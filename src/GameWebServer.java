import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Dependency-free local HTTP adapter. Sessions live in memory until exit. */
public final class GameWebServer {
    private static final Map<String,GameEngine> games = Collections.synchronizedMap(new LinkedHashMap<>() {
        protected boolean removeEldestEntry(Map.Entry<String,GameEngine> e) {return size()>100;}
    });
    public static void main(String[] args) throws IOException {
        int port=args.length==0?8082:Integer.parseInt(args[0]);
        HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",port),0);
        Path web=Path.of("web").toAbsolutePath().normalize();
        server.createContext("/",x->{
            try {
                String path=x.getRequestURI().getPath();
                if(path.startsWith("/api/")) {
                    if(!x.getRequestMethod().equals("POST")){x.getResponseHeaders().set("Allow","POST");send(x,405,"application/json","{\"error\":\"POST required\"}");return;}
                    Map<String,String> p=query(x.getRequestURI().getRawQuery());
                    if(path.equals("/api/new")) {
                        long seed;
                        try{seed=Long.parseLong(p.getOrDefault("seed",""));}catch(NumberFormatException e){throw new IllegalArgumentException("Seed must be a signed 64-bit whole number");}
                        GameEngine game=new GameEngine(p.getOrDefault("name",""),p.getOrDefault("difficulty",""),seed);
                        String id=UUID.randomUUID().toString();games.put(id,game);send(x,200,"application/json",json(game,id));
                    } else if(path.equals("/api/turn")) {
                        String id=p.getOrDefault("id","");GameEngine game=games.get(id);
                        if(game==null){send(x,404,"application/json","{\"error\":\"Expedition expired. Start a new one.\"}");return;}
                        synchronized(game){game.nextRound(Integer.parseInt(p.getOrDefault("revision","-1")));send(x,200,"application/json",json(game,id));}
                    } else send(x,404,"application/json","{\"error\":\"Not found\"}");
                    return;
                }
                if(!x.getRequestMethod().equals("GET")){send(x,405,"text/plain","GET required");return;}
                Path file=web.resolve(path.equals("/")?"index.html":path.substring(1)).normalize();
                if(!file.startsWith(web)||!Files.isRegularFile(file)){send(x,404,"text/plain","Not found");return;}
                String mime=file.toString().endsWith(".html")?"text/html":file.toString().endsWith(".css")?"text/css":file.toString().endsWith(".js")?"text/javascript":file.toString().endsWith(".svg")?"image/svg+xml":"application/octet-stream";
                byte[] bytes=Files.readAllBytes(file);headers(x,mime);x.sendResponseHeaders(200,bytes.length);x.getResponseBody().write(bytes);
            } catch(IllegalArgumentException e){send(x,400,"application/json","{\"error\":"+quote(e.getMessage())+"}");}
            catch(Exception e){System.err.println(e.getClass().getSimpleName());send(x,500,"application/json","{\"error\":\"Unable to continue. Check the server terminal.\"}");}
            finally{x.close();}
        });
        server.start();System.out.println("Wild Trails ready: http://127.0.0.1:"+port+" | Ctrl+C to stop");
    }
    private static void headers(HttpExchange x,String mime){
        x.getResponseHeaders().set("Content-Type",mime+"; charset=utf-8");x.getResponseHeaders().set("Cache-Control","no-store");x.getResponseHeaders().set("X-Content-Type-Options","nosniff");
        x.getResponseHeaders().set("Content-Security-Policy","default-src 'self'; style-src 'self'; script-src 'self'; img-src 'self' data:; object-src 'none'; frame-ancestors 'none'");
    }
    private static void send(HttpExchange x,int code,String mime,String text)throws IOException{byte[] b=text.getBytes(StandardCharsets.UTF_8);headers(x,mime);x.sendResponseHeaders(code,b.length);x.getResponseBody().write(b);}
    static Map<String,String> query(String query){
        if(query==null||query.length()>1024)throw new IllegalArgumentException("Invalid inputs");
        Map<String,String> values=new HashMap<>();
        for(String part:query.split("&")){String[] pair=part.split("=",2);if(pair.length!=2)throw new IllegalArgumentException("Invalid inputs");String key=URLDecoder.decode(pair[0],StandardCharsets.UTF_8);if(values.put(key,URLDecoder.decode(pair[1],StandardCharsets.UTF_8))!=null)throw new IllegalArgumentException("Duplicate input");}return values;
    }
    static String quote(String text){StringBuilder out=new StringBuilder("\"");for(char c:text.toCharArray()){if(c=='"'||c=='\\')out.append('\\');if(c<32)out.append(String.format("\\u%04x",(int)c));else out.append(c);}return out.append('"').toString();}
    static String json(GameEngine game,String id){
        synchronized(game){
            StringJoiner cells=new StringJoiner(",","[","]");for(char c:game.getTrail())cells.add(quote(String.valueOf(c)));
            StringJoiner players=new StringJoiner(",","[","]");for(int i=0;i<2;i++){Player p=game.getPlayer(i);players.add("{\"name\":"+quote(p.getName())+",\"position\":"+p.getPosition()+",\"score\":"+p.getScore()+",\"sightings\":"+Arrays.toString(game.getSightings(i))+"}");}
            StringJoiner events=new StringJoiner(",","[","]");for(GameEngine.Turn t:game.getTurns())events.add("{\"actor\":"+t.actor()+",\"roll\":"+t.roll()+",\"from\":"+t.from()+",\"landed\":"+t.landed()+",\"to\":"+t.to()+",\"animal\":"+quote(t.animal())+",\"points\":"+t.points()+",\"feature\":"+quote(t.feature())+",\"shift\":"+t.shift()+",\"finished\":"+t.finished()+"}");
            return "{\"id\":"+quote(id)+",\"seed\":"+quote(Long.toString(game.getSeed()))+",\"difficulty\":"+quote(game.getDifficulty())+",\"revision\":"+game.getRevision()+",\"over\":"+game.isOver()+",\"winner\":"+game.getWinner()+",\"finisher\":"+game.getFinisher()+",\"trail\":"+cells+",\"players\":"+players+",\"events\":"+events+"}";
        }
    }
}
