'use strict';
const $=id=>document.getElementById(id),form=$('setup');
const species=[['Koala','🐨',10],['Emu','🪶',7],['Wombat','🐾',5],['Kangaroo','🦘',2],['Rabbit','🐇',-5]];
const features={_:{icon:'·',name:'Open trail',move:0},c:{icon:'≈',name:'Creek',move:-2},b:{icon:'⌁',name:'Bridge',move:4},f:{icon:'▰',name:'Fallen tree',move:-3},l:{icon:'▱',name:'Landslide',move:-5}};
const esc=v=>String(v).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const signed=n=>(n>0?'+':'')+n;
let game=null,busy=false,draft=false;
function settings(){return{name:$('name').value.trim(),difficulty:new FormData(form).get('difficulty'),seed:$('seed').value};}
function lock(value){busy=value;$('start').disabled=value;$('roll').disabled=value||!game||game.over;$('replay').disabled=value;$('export').disabled=value||!game;}
async function api(route,params){
 const r=await fetch('/api/'+route+'?'+new URLSearchParams(params),{method:'POST',signal:AbortSignal.timeout(15000)});
 const json=await r.json();if(!r.ok)throw new Error(json.error||'Unable to continue');return json;
}
async function start(event,repeat=false){
 if(event)event.preventDefault();if(busy)return;if(!repeat&&!form.reportValidity())return;
 const config=repeat&&game?{name:game.players[0].name,difficulty:game.difficulty,seed:game.seed}:settings();
 if(!/^-?\d+$/.test(config.seed)){$('error').textContent='Use a whole number for the seed.';return;}
 lock(true);$('error').textContent='';
 try{const next=await api('new',config);game=next;draft=false;
  $('name').value=game.players[0].name;$('seed').value=game.seed;form.querySelector('input[name="difficulty"][value="'+game.difficulty+'"]').checked=true;
  render();$('turnTitle').textContent='Your move, '+game.players[0].name+'.';$('turnText').textContent='Roll a four-sided die. The ranger takes a turn after you.';$('die').textContent='?';
 }catch(e){$('error').textContent=e.name==='TimeoutError'?'Request timed out. Check the server and try again.':e.message;}
 finally{lock(false);}
}
async function roll(){
 if(busy||!game||game.over)return;
 lock(true);$('error').textContent='';$('die').classList.add('rolling');
 const previous=game.events.length;
 try{game=await api('turn',{id:game.id,revision:game.revision});render();
  const turns=game.events.slice(previous),human=turns[0];$('die').textContent=human.roll;
  $('turnTitle').textContent=game.over?'The expedition is complete.':game.players[0].name+' rolled '+human.roll+'.';
  $('turnText').textContent=turns.map(t=>game.players[t.actor].name+': tile '+(t.to+1)+(t.animal?' · '+t.animal+' '+signed(t.points):' · no sighting')+(t.feature?' · '+t.feature:'')).join(' / ');
 }catch(e){$('error').textContent=(e.name==='TimeoutError'?'Request timed out. ':e.message+' ')+'If the roll reached the server, start a new expedition to resync.';}
 finally{$('die').classList.remove('rolling');lock(false);}
}
function board(){
 const trail=game?game.trail:Array(18).fill('_'),cols=6,rows=Math.ceil(trail.length/cols);
 $('board').style.gridTemplateRows='repeat('+rows+', minmax(55px, 1fr))';
 $('board').innerHTML=trail.map((cell,i)=>{
  const row=Math.floor(i/cols),col=row%2===0?i%cols:cols-1-i%cols;
  const start=i===0,finish=i===trail.length-1,feature=features[cell];
  const people=game?game.players.map((p,index)=>p.position===i?'<i class="marker '+(index===0?'you':'ranger')+'" title="'+esc(p.name)+'">'+(index===0?'E':'R')+'</i>':'').join(''):'';
  return '<div class="tile feature-'+cell+(start?' start':'')+(finish?' finish last':'')+(row%2?' reverse':'')+(i%cols===cols-1?' row-end':'')+(people?' current':'')+'" data-row="'+(row+1)+'" data-col="'+(col+1)+'" title="Tile '+(i+1)+': '+feature.name+(feature.move?' ('+signed(feature.move)+' spaces)':'')+'"><span class="tile-num">'+String(i+1).padStart(2,'0')+'</span><span class="tile-icon" aria-hidden="true">'+(start?'⚑':finish?'✦':feature.icon)+'</span><span class="tile-label">'+(start?'START':finish?'FINISH':cell==='_'?'':feature.name)+'</span><span class="tokens">'+people+'</span></div>';
 }).join('');
 $('board').querySelectorAll('.tile').forEach(tile=>{tile.style.gridRow=tile.dataset.row;tile.style.gridColumn=tile.dataset.col;});
}
function journal(){
 $('animalCards').innerHTML=species.map(([name,icon,points],i)=>{
 const yours=game?game.players[0].sightings[i]:0,rangers=game?game.players[1].sightings[i]:0;
 return '<article class="animal-card '+(yours?'spotted':'')+'"><span aria-hidden="true">'+icon+'</span><h3>'+name+'</h3><p class="points '+(points<0?'negative':'')+'">'+signed(points)+' points</p><div class="counts"><span>You: '+yours+'</span><span>Ranger: '+rangers+'</span></div></article>';
 }).join('');
}
function render(){
 board();journal();if(!game)return;
 $('trailTitle').textContent=game.difficulty==='easy'?'SECTOR 01 / MEADOW LOOP':'SECTOR 02 / WILD COUNTRY';
 $('round').textContent=(game.over?'Complete · ':'Round ')+(game.over?Math.ceil(game.revision/2):Math.floor(game.revision/2)+1)+' · '+game.trail.length+' tiles';
 $('playerName').textContent=game.players[0].name;
 for(const [i,prefix] of [[0,'player'],[1,'ranger']]){
  const player=game.players[i];$(prefix+'Position').textContent='Tile '+(player.position+1)+' / '+game.trail.length;
  $(prefix+'Score').innerHTML=player.score+'<small>POINTS</small>';
 }
 $('log').innerHTML=game.events.length?game.events.slice().reverse().map((t,index)=>{
  const number=game.events.length-index;
  return '<li><span>#'+String(number).padStart(2,'0')+'</span><span class="event-who">'+esc(game.players[t.actor].name)+'</span><span class="event-copy">Rolled <b>'+t.roll+'</b> · tile '+(t.from+1)+' → '+(t.landed+1)+(t.feature?' · '+esc(t.feature)+' '+signed(t.shift)+' → '+(t.to+1):'')+'<br>'+(t.animal?esc(t.animal)+' sighted.':'No animal sighted.')+(t.finished?' <b>Finish bonus +10.</b>':'')+'</span><span class="event-points '+(t.points<0?'negative':'')+'">'+signed(t.points+(t.finished?10:0))+'</span></li>';
 }).join(''):'<li class="empty-log">Your expedition has begun. Roll to make the first discovery.</li>';
 $('result').hidden=!game.over;
 if(game.over){$('resultTitle').textContent=game.winner===-1?'EXPEDITION COMPLETE / DRAW':game.players[game.winner].name+' wins!';
 $('resultText').textContent=(game.finisher<0?'Turn limit reached.':game.players[game.finisher].name+' reached the finish and earned +10.')+' Final score: '+game.players[0].score+' : '+game.players[1].score+'. Highest score wins.';}
 $('start').innerHTML=game?'New expedition <span>↗</span>':'Launch expedition <span>↗</span>';
}
form.addEventListener('submit',start);
form.addEventListener('input',()=>{draft=true;if(game)$('turnText').textContent='Setup changed. Your current expedition is unchanged until you press New expedition.';});
$('shuffle').addEventListener('click',()=>{$('seed').value=Math.floor(Math.random()*1000000);draft=true;if(game)$('turnText').textContent='New seed selected. Press New expedition to apply.';});
$('roll').addEventListener('click',roll);$('replay').addEventListener('click',()=>start(null,true));
$('rulesOpen').addEventListener('click',()=>$('rules').showModal());$('rulesClose').addEventListener('click',()=>$('rules').close());
$('export').addEventListener('click',()=>{
 if(!game)return;
 const report=['WILD TRAILS — EXPEDITION JOURNAL','Player: '+game.players[0].name,'Difficulty: '+game.difficulty,'Seed: '+game.seed,'Status: '+(game.over?'Complete':'In progress'),'Scores: '+game.players.map(p=>p.name+' '+p.score).join(' / '),'Trail: '+game.trail.join(''),''];
 for(const t of game.events)report.push(game.players[t.actor].name+' | die '+t.roll+' | tile '+(t.from+1)+' -> '+(t.landed+1)+' -> '+(t.to+1)+' | '+(t.animal||'No sighting')+' '+signed(t.points)+' | '+(t.feature||'No feature')+(t.finished?' | finish +10':''));
 const url=URL.createObjectURL(new Blob([report.join('\n')],{type:'text/plain;charset=utf-8'})),link=document.createElement('a');link.href=url;link.download='wild-trails-'+game.seed+'.txt';link.click();setTimeout(()=>URL.revokeObjectURL(url),1000);
});
board();journal();
