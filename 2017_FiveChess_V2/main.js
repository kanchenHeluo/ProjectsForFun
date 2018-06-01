$(document).ready(function(){
	$(".start").click(startGame);
	$(".regret").click(regretGame);
	$('.chessboard').click(posChess);

	var chessboardSetting = {
		start_x: 50,
		start_y: 15,
		len: 25,
		cnt: 15
	}

	var chessboardInfo = {
		chessboardArr: [],
		current_type: true, //true->black, false->white
		last_position: {'x':-1,'y':-1},
		last_position2: {'x':-1, 'y':-1}
	}

	var myCanvas=$("#myCanvas").get(0);
    var chessboard=myCanvas.getContext("2d");
    var tracePos = new Array();


	function drawChess(type, i, j){
	  if(type){
	  	chessboard.fillStyle="#000";
	  }else{
	  	chessboard.fillStyle="#FFF";
	  }
	  
	  var x = j*chessboardSetting.len;
	  var y = i*chessboardSetting.len;
	  chessboard.beginPath();
	  chessboard.arc(x,y,10,0,Math.PI*2,true);
	  chessboard.fill();

	  chessboard.strokeStyle="#000";
	  chessboard.stroke();
	}

	function drawChessBoard() {
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
	}

	function init(){
		drawChessBoard();
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
  		chessboardInfo.last_position2 = {'x':-1,'y':-1};
	};


	function posChess(e){
		var pos = getIndexFromPos(e.offsetX, e.offsetY);
		if (chessboardInfo.chessboardArr[pos.i][pos.j]!=-1) {
			return ;
		}
		drawChess(chessboardInfo.current_type, pos.i, pos.j);	
	  	tracePos.push({"i":pos.i, "j":pos.j});
		chessboardInfo.chessboardArr[pos.i][pos.j] = chessboardInfo.current_type ? 1:0;	
		if(judgeGame(chessboardInfo.current_type, pos, chessboardInfo.chessboardArr)){
			endGame();
		}else{
			chessboardInfo.current_type = !chessboardInfo.current_type;
			chessboardInfo.last_position2 = chessboardInfo.last_position;
			chessboardInfo.last_position = pos;

			var rivalpos = getAIPos(chessboardInfo.chessboardArr,chessboardInfo.current_type);
			drawChess(chessboardInfo.current_type, rivalpos.i, rivalpos.j);	
			tracePos.push({"i":rivalpos.i, "j":rivalpos.j});
			chessboardInfo.chessboardArr[rivalpos.i][rivalpos.j] = chessboardInfo.current_type ? 1:0;	
			if(judgeGame(chessboardInfo.current_type, rivalpos, chessboardInfo.chessboardArr)){
				endGame();
			}else{
				chessboardInfo.current_type = !chessboardInfo.current_type;
				chessboardInfo.last_position2 = chessboardInfo.last_position;
				chessboardInfo.last_position = rivalpos;
			}			
		}		
	}

	function getIndexFromPos(x,y){
		var i = parseInt((y-chessboardSetting.start_y+chessboardSetting.len/2)/chessboardSetting.len);
		var j = parseInt((x-chessboardSetting.start_x+chessboardSetting.len/2)/chessboardSetting.len);
		return {'i':i, 'j':j};
	}

	function judgeDir(horizontal, vertical, lrfalling, rlfalling, type, lastpos, arr){
		var cnt = 0;
		var val = type ? 1:0;
		var x = lastpos.i, y = lastpos.j;
		var l = arr.length;
		while(x>=0 && y>=0 && x<l){
			if(arr[x][y] != val){
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
		x = lastpos.i;
		y = lastpos.j;
		while(x>=0 && y<l && x<l){
			if(arr[x][y] != val){
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

	function judgeGame(type, pos, arr){
		return judgeDir(true,false,false,false, type, pos, arr) || judgeDir(false,true,false,false, type, pos, arr) 
			|| judgeDir(false,false,true,false, type, pos, arr) || judgeDir(false,false,false,true, type, pos, arr);
	}

	function regretGame(){
		if(tracePos && tracePos.length > 0){
			chessboardInfo.chessboardArr[ tracePos[tracePos.length-1].i ][ tracePos[tracePos.length-1].j ] = -1;
			tracePos.pop();
			chessboardInfo.chessboardArr[ tracePos[tracePos.length-1].i ][ tracePos[tracePos.length-1].j ] = -1;
			tracePos.pop();
			
			drawChessBoard();
			for (var i=0; i<tracePos.length; ++i) {
				drawChess(
					(chessboardInfo.chessboardArr[ tracePos[i].i ][ tracePos[i].j ] == 1), 
					tracePos[i].i, 
					tracePos[i].j);
			}
		}
		
	}

	function startGame(){
		init();
	}

	function endGame(){
		if(chessboardInfo.current_type){
			$('.result.black').show();
			$('.result.white').hide();
		}else{
			$('.result.white').show();
			$('.result.black').hide();
		}		
	}

	init();

	//AI 5 Chess logic
	var DEFAULTMINSCORE = 0, DEFAULTMAXSCORE = 1;
	var steps = 4;
	var maxScore, maxPos;
	var lp1, lp2;

	function getAIPos(arr, type){
		var l = arr.length;
		lp1 = chessboardInfo.last_position;
		lp2 = chessboardInfo.last_position2;
		dfs(arr, type, lp1, lp2, 1); 
		return maxPos;
	}

	function getPrePos(dir, i, j, l){
		var pos = null;
		if(dir == 0){ //rl
			if(j<l-1&&i>0){
				pos = {"i":i-1,"j":j+1};
			}

		}else if(dir == 1){ //vertical
			if(i>0){
				pos = {"i":i-1,"j":j};
			}
		}else if(dir == 2){ //lr
			if(j>0&&i>0){
				pos = {"i":i-1,"j":j-1};
			}

		}else{//horizontal
			if(j>0){
				pos = {"i":i,"j":j-1};
			}
		}
		return pos;
	}

	function getNextPos(dir, i, j, l){
		var pos = null;
		if(dir == 0){ //rl
			if(i<l-1&&j>0){
				pos = {"j":j-1,"i":i+1};
			}
		}else if(dir == 1){ //vertical
			if(i<l-1){
				pos = {"i":i+1,"j":j};
			}
		}else if(dir == 2){ //lr
			if(j<l-1&&i<l-1){
				pos = {"i":i+1,"j":j+1};
			}
		}else{//horizontal
			if(j<l-1){
				pos = {"i":i,"j":j+1};
			}
		}
		return pos;
	}

	function getChessLen(i, j, dir, cntArr, arr, type){
		var val = type? 1:0;

		var prepos = getPrePos(dir, i, j, arr.length);
		if(prepos == null || (prepos!=null && arr[prepos.i][prepos.j] != val)){
			var c = 1;
			var nextpos = getNextPos(dir,i,j, arr.length);
			
			while(nextpos != null && arr[nextpos.i][nextpos.j] == val){
				c++;
				if(c==5){				
					break;
				}
				nextpos = getNextPos(dir,nextpos.i,nextpos.j, arr.length);
			}
			if(c == 5){
				cntArr[5][0] ++; 
			}else{
				if(prepos == null || (prepos!=null && arr[prepos.i][prepos.j] == 1-val)){

					if(nextpos!=null && arr[nextpos.i][nextpos.j] == -1){ // to be refine
						cntArr[c][1] ++;
					}
				}else{ // two sides
					if(nextpos!=null && arr[nextpos.i][nextpos.j] == -1){
						cntArr[c][0]++;
					}else{
						cntArr[c][1] ++;
					}
				}
			}
			
		}
		return cntArr;
	}

	//cal score for black- type[true] - val[1], to be refine cntArr from overall
	function calcChessBoardScoreForType(arr, type){
		var cntArr = new Array()
		for(var i=1;i<6;i++){
			cntArr[i] = new Array();
			cntArr[i][0] = 0;
			cntArr[i][1] = 0;
		}
		var val = type ? 1:0;

		var l = arr.length;
		for(var i=0; i<l; i++){
			for(var j=0; j<l; j++){
				if(arr[i][j] == val){
					cntArr = getChessLen(i, j, 0, cntArr, arr, type);
					cntArr = getChessLen(i, j, 1, cntArr, arr, type);
					cntArr = getChessLen(i, j, 2, cntArr, arr, type);
					cntArr = getChessLen(i, j, 3, cntArr, arr, type);
				}			
			}
		}
		return cntArr;

	}

	function getScore(cnt){
		var score = 0;
		score += (cnt[3][0] + cnt[4][1])*10;
		score += (cnt[3][1] + cnt[2][0])*5;
		score += (cnt[2][1] + cnt[1][0])*1;
		return score;
	}

	function bestLine(arr) {
		if (arr[5][0] > 0 || arr[4][0] > 0) 
			return 3;
		else if (arr[3][0] > 0 || arr[4][1] > 0) 
			return 2;
		else if (arr[2][0] > 0 || arr[3][1] > 0) 
			return 1;
		else return 0;
	}

	function calChessBoardScore(arr, is_black_going){
		var bcnt = calcChessBoardScoreForType(arr, true);
		var wcnt = calcChessBoardScoreForType(arr, false);

		var bscore = bestLine(bcnt);
		var wscore = bestLine(wcnt);
		if (is_black_going) {
			bscore += 0.5;
		} else {
			wscore += 0.5;
		}

		var final_score = 0.5;
		if (bscore > wscore) {
			final_score = (bscore + 3.0) / 6.0;
		} else if (bscore < wscore) {
			final_score = 1.0 - (wscore + 3.0) / 6.0;
		} else {
			return 0.5;
		}

		if (final_score > 1.0) final_score = 1.0;
		if (final_score < 0) final_score = 0;
		return final_score;
	}

	function getAvaiPos(arr, lp1, lp2){
		var poses = new Array(), ret = new Array(), f={};
		var l = arr.length;
		/*
		var i = lp1.i, j = lp1.j;
		for (var m=-1; m<=1; ++m) {
			for (var n=-1; n<=1; ++n) {
				if (i+m>=0 && i+m<l && j+n>=0 && j+n<l && arr[i+m][j+n] == -1) {
					poses.push({"i":i+m, "j":j+n});
				}
			}
		}
		i = lp2.i;
		j = lp2.j;
		for (var m=-1; m<=1; ++m) {
			for (var n=-1; n<=1; ++n) {
				if (i+m>=0 && i+m<l && j+n>=0 && j+n<l && arr[i+m][j+n] == -1) {
					poses.push({"i":i+m, "j":j+n});
				}
			}
		}
*/

		for(i=0; i<l; i++){
			for(j=0; j<l; j++){
				if(arr[i][j] == -1){
					var added = false;
					for (var m=-1; m<=1; ++m) {
						for (var n=-1; n<=1; ++n) {
							if (i+m>=0 && i+m<l && j+n>=0 && j+n<l && arr[i+m][j+n] != -1) {
								added = true;
								break;
							}
						}
						if (added) {
							break;
						}
					}
					if (added) {
						poses.push({"i":i, "j":j});
					}	
				}
			}
		}
/*
		var l = poses.length;
		for(i=0;i<l;i++){
			if(!f[poses[i].i+'_'+poses[i].j]){
				ret.push(poses[i]);
				f[poses[i].i+'_'+poses[i].j] = true;
			}
		}

		return ret;*/
		return poses;
	}

	function dfs(arr, type, lastpos, lastpos2, r){
		if(r > steps || judgeGame(!type, lastpos, arr)){
			return calChessBoardScore(arr, type)
		}else{
			var poses = getAvaiPos(arr, lastpos, lastpos2);
			if(poses.length == 0){
				var idx = parseInt(arr.length/2)
				poses.push({"i":idx,"j":idx});
			}
			
			var maxscore = type ? DEFAULTMINSCORE : DEFAULTMAXSCORE;
			var maxpos = poses[0];
			for(var i=0; i<poses.length; i++){
				arr[poses[i].i][poses[i].j] = type ? 1:0;
				var score = dfs(arr, !type, poses[i], lastpos, r+1);
				if(type && score>maxscore){
					maxscore = score;
					maxpos = poses[i];
				}
				if(!type && score<maxscore){
					maxscore = score;
					maxpos = poses[i];
				}
				arr[poses[i].i][poses[i].j] = -1;
				if((type && maxscore == DEFAULTMAXSCORE) || (!type && maxscore == DEFAULTMINSCORE)){
					break;
				}
			}
			maxScore = maxscore;
			maxPos = maxpos;
			return maxscore;
		}
	}
});
