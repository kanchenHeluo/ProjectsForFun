$(document).ready(function(){
	$(".start").click(startGame);
	$('.chessboard').click(posChess);

	var chessboardSetting = {
		start_x: 50,
		start_y: 15,
		len: 25,
		cnt: 25
	}

	var chessboardInfo = {
		chessboardArr: [],
		current_type: true, //true->black, false->white
		last_position: {'x':-1,'y':-1}
	}

	var myCanvas=$("#myCanvas").get(0);
    var chessboard=myCanvas.getContext("2d");

	function init(){
		myCanvas.width = chessboardSetting.start_x + chessboardSetting.len*chessboardSetting.cnt + chessboardSetting.len;
		myCanvas.height = chessboardSetting.start_y + chessboardSetting.len*chessboardSetting.cnt + chessboardSetting.len;

		chessboard.translate(chessboardSetting.start_x, chessboardSetting.start_y);
		chessboard.strokeStyle = "#000";
		
		for(var i=0; i<chessboardSetting.cnt; i++){
			chessboard.moveTo(0,i*chessboardSetting.len);
			chessboard.lineTo((chessboardSetting.cnt-1)*chessboardSetting.len, i*chessboardSetting.len);			
		}

		for(var i=0; i<chessboardSetting.cnt; i++){
			chessboard.moveTo(i*chessboardSetting.len, 0);
			chessboard.lineTo(i*chessboardSetting.len, (chessboardSetting.cnt-1)*chessboardSetting.len);			
		}
		chessboard.stroke();
		initChessboardInfo();
	};

	function initChessboardInfo(){
		chessboardInfo.chessboardArr = new Array();
		for(var i=0;i<chessboardSetting.cnt;i++){
		    chessboardInfo.chessboardArr[i]=new Array();
		    for(var j=0;j<chessboardSetting.cnt;j++){
		      	chessboardInfo.chessboardArr[i][j]=-1;
		    }
  		}
  		chessboardInfo.current_type = true;
  		chessboardInfo.last_position = {'x':-1,'y':-1};
	};
	
	function drawChess(type, i, j){
	  if(type){
	  	chessboard.fillStyle="#000";
	  	chessboardInfo.chessboardArr[i][j] = 1;
	  }else{
	  	chessboard.fillStyle="#FFF";
	  	chessboardInfo.chessboardArr[i][j] = 0;
	  }
	  
	  var x = j*chessboardSetting.len;
	  var y = i*chessboardSetting.len;
	  chessboard.beginPath();
	  chessboard.arc(x,y,10,0,Math.PI*2,true);
	  chessboard.fill();

	  chessboard.strokeStyle="#000";
	  chessboard.stroke();
	}

	function posChess(e){
		var pos = getIndexFromPos(e.offsetX, e.offsetY);
		drawChess(chessboardInfo.current_type, pos.i, pos.j);
		chessboardInfo.current_type = !chessboardInfo.current_type;
		chessboardInfo.last_position = pos;
		if(judgeGame()){
			endGame();
		}
	}

	function getIndexFromPos(x,y){
		var i = parseInt((y-chessboardSetting.start_y+chessboardSetting.len/2)/chessboardSetting.len);
		var j = parseInt((x-chessboardSetting.start_x+chessboardSetting.len/2)/chessboardSetting.len);
		return {'i':i, 'j':j};
	}

	function judgeDir(horizontal, vertical, lrfalling, rlfalling){
		var cnt = 0;
		var val = chessboardInfo.current_type ? 0:1;
		var x = chessboardInfo.last_position.i, y = chessboardInfo.last_position.j;
		while(x>=0 && y>=0 && x<chessboardSetting.cnt){
			if(chessboardInfo.chessboardArr[x][y] != val){
				break;								
			}
			cnt++;
			if(!horizontal){
				x = vertical || lrfalling ? x-1: x+1;					
			}
			if(!vertical){
				y--;
			}
		}
		x = chessboardInfo.last_position.i;
		y = chessboardInfo.last_position.j;
		while(x>=0 && y<chessboardSetting.cnt && x<chessboardSetting.cnt){
			if(chessboardInfo.chessboardArr[x][y] != val){
				break;
			}
			cnt++;
			if(!horizontal){
				x = lrfalling || vertical ? x+1: x-1;				
			}
			if(!vertical){
				y++;
			}
		}
		return cnt>5;
	}

	function judgeGame(){
		return judgeDir(true,false,false,false) || judgeDir(false,true,false,false) || judgeDir(false,false,true,false) || judgeDir(false,false,false,true);
	}

	function startGame(){
		init();
	}

	function endGame(){
		if(!chessboardInfo.current_type){
			$('.result.black').show();
			$('.result.white').hide();
		}else{
			$('.result.white').show();
			$('.result.black').hide();
		}		
	}
	init();

});
