//AI logic
	var DEFAULTMINSCORE = 0, DEFAULTMAXSCORE = 1;
	var steps = 5;
	var maxScore, maxPos;

	$('.test').click(function(){
		/*
		var pos = {"i":0,"j":0};
		var rlpos = getNextPos(0, pos.i,pos.j,5);
		var verticalpos = getNextPos(1,pos.i,pos.j,5);
		var lrpos = getNextPos(2,pos.i,pos.j,5);
		var horizontalpos = getNextPos(3,pos.i,pos.j,5);
		if(rlpos!=null){
			console.log(rlpos.i, rlpos.j);
		}else{
			console.log('null pre rl');
		}
		if(verticalpos!=null){
			console.log(verticalpos.i, verticalpos.j);
		}else{
			console.log('null pre vertical');
		}
		if(lrpos!=null){
			console.log(lrpos.i, lrpos.j);
		}else{
			console.log('null pre lr');
		}
		if(horizontalpos!=null){
			console.log(horizontalpos.i, horizontalpos.j);
		}else{
			console.log('null pre horizontal');
		}*/
		/*
		var pos = {"i":0,"j":1};
		var arr = [
			[0,0,0,-1,-1,-1],
			[1,0,-1,1,-1,-1],
			[-1,0,1,1,1,1],
			[-1,-1,1,1,-1,-1],
			[-1,-1,0,-1,1,-1],
			[-1,-1,-1,-1,-1,1]	
		];
		var cntArr = new Array()
		
		for(var i=1;i<6;i++){
			cntArr[i] = new Array();
			cntArr[i][0] = 0;
			cntArr[i][1] = 0;
		}
		cntArr = getChessLen(pos.i, pos.j, 0, cntArr, arr, false);
		console.log('dir rl');
		for(var i=1; i<5;i++){
			console.log(cntArr[i][0]+','+cntArr[i][1]);
		}
		console.log(cntArr[5][0]);

		for(var i=1;i<6;i++){
			cntArr[i] = new Array();
			cntArr[i][0] = 0;
			cntArr[i][1] = 0;
		}
		cntArr = getChessLen(pos.i, pos.j, 1, cntArr, arr, false);
		console.log('dir vertical');
		for(var i=1; i<5;i++){
			console.log(cntArr[i][0]+','+cntArr[i][1]);
		}
		console.log(cntArr[5][0]);

		for(var i=1;i<6;i++){
			cntArr[i] = new Array();
			cntArr[i][0] = 0;
			cntArr[i][1] = 0;
		}
		cntArr = getChessLen(pos.i, pos.j, 2, cntArr, arr, false);
		console.log('dir lr');
		for(var i=1; i<5;i++){
			console.log(cntArr[i][0]+','+cntArr[i][1]);
		}
		console.log(cntArr[5][0]);

		for(var i=1;i<6;i++){
			cntArr[i] = new Array();
			cntArr[i][0] = 0;
			cntArr[i][1] = 0;
		}
		cntArr = getChessLen(pos.i, pos.j, 3, cntArr, arr, false);
		console.log('dir horizontal');
		for(var i=1; i<5;i++){
			console.log(cntArr[i][0]+','+cntArr[i][1]);
		}
		console.log(cntArr[5][0]);
		*/
		/*
		var arr = [
			[ 0, 0, 0,-1,-1,-1],
			[ 1, 0,-1, 1,-1,-1],
			[-1, 0, 1, 1, 1, 1],
			[-1,-1, 1, 1,-1,-1],
			[-1,-1, 0,-1, 1,-1],
			[-1,-1,-1,-1,-1, 1]	
		];
		var cntArr = new Array()
		
		for(var i=1;i<6;i++){
			cntArr[i] = new Array();
			cntArr[i][0] = 0;
			cntArr[i][1] = 0;
		}
		cntArr = calcChessBoardScoreForType(arr, true);
		for(var i=1; i<5;i++){
			console.log(cntArr[i][0]+','+cntArr[i][1]);
		}
		console.log(cntArr[5][0]);
		*/
	});

	function getAIPos(arr, type){
		var l = arr.length;
		lastpos = chessboardInfo.last_position;
		dfs(arr, type, lastpos, 1); 
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
	function calChessBoardScore(arr){
		var bcnt = calcChessBoardScoreForType(arr, true);
		var wcnt = calcChessBoardScoreForType(arr, false);

		if(wcnt[5][0] > 0 || wcnt[4][0] > 0) return 0;
		if(bcnt[5][0] > 0 || bcnt[4][0] > 0) return 1;

		var bscore = getScore(bcnt);
		var wscore = getScore(wcnt);
		return bscore/(bscore+wscore);
	}

	function getAvaiPos(arr){
		var poses = new Array(), flag = new Array();
		var l = arr.length;

		for(var i=0; i<l; i++){
			flag[i] = new Array()
			for(var j=0; j<l; j++){
				flag[i][j] = false;
			}
		}
		for(var i=0; i<l; i++){
			for(var j=0; j<l; j++){
				if(arr[i][j] != -1){		
					if(i>0){
						if(j>0){
							flag[i-1][j-1] = (arr[i-1][j-1] == -1);
						}
						if(j<l-1){
							flag[i-1][j+1] = (arr[i-1][j+1] == -1);
						}
						flag[i-1][j] = (arr[i-1][j] == -1);
					}
					if(i<l-1){
						if(j<l-1){
							flag[i+1][j+1] = (arr[i+1][j+1] == -1);
						}
						if(j>0){
							flag[i+1][j-1] = (arr[i+1][j-1] == -1);
						}
						flag[i+1][j] = (arr[i+1][j] == -1);
					}
					if(j>0){
						flag[i][j-1] = (arr[i][j-1] == -1);
					}
					if(j<l-1){
						flag[i][j+1] = (arr[i][j+1] == -1);
					}
				}
			}
		}
		for(var i=0; i<l; i++){
			for(var j=0; j<l; j++){
				if(flag[i][j]){
					poses.push({"i": i,"j":j});
				}
			}
		}
		return poses;
	}

	function dfs(arr, type, lastpos, r){
		if(r > steps || judgeGame(!type, lastpos, arr)){
			return calChessBoardScore(arr)
		}else{
			var poses = getAvaiPos(arr);
			if(poses.length == 0){
				var idx = parseInt(arr.length/2)
				poses.push({"i":idx,"j":idx});
			}
			var maxscore = type ? DEFAULTMINSCORE : DEFAULTMAXSCORE, maxpos = 0;
			for(var i=0; i<poses.length; i++){
				arr[poses[i].i][poses[i].j] = type ? 1:0;
				var score = dfs(arr, !type, poses[i], r+1);
				if(type && score>maxscore){
					maxscore = score;
					maxpos = poses[i];
				}
				if(!type && score<maxscore){
					maxscore = score;
					maxpos = poses[i];
				}
				arr[poses[i].i][poses[i].j] = -1;
			}
			maxScore = maxscore;
			maxPos = maxpos;
			return maxscore;
		}
	}