angular.module("ckApp")
.controller("game1024Ctrl", ["$scope", function ($scope) {
    /*************************for drawing cubics*****************************/
    /**settig info**/
    $scope.settingInfo = {
        blocked: false,
        blockedNumber: 1,
        scale: 4,
        score: 0
    };
    $scope.cubicLen = parseInt($scope.settingInfo.scale) * parseInt($scope.settingInfo.scale) + 1;
    $scope.items = [];
    $scope.initItems = function() {
        $scope.items = [];
        for (var i = 0; i < parseInt($scope.settingInfo.scale); i++) {
            $scope.items.push(i);
        }
    };
    $scope.$watch('settingInfo.scale', function() {
        $scope.cubicLen = parseInt($scope.settingInfo.scale) * parseInt($scope.settingInfo.scale) + 1;
        $scope.initItems();
    });
    /***************************************************************************/
    /*******************for cubics move logic***********************************/
    $scope.cubics = [];
    $scope.availableIndex = [];

    $scope.randomCubic = function() {
        if ($scope.settingInfo.score > 5000) {
            randomIndexSetValue([2, 4]);
        } else {
            randomIndexSetValue([2]);
        }
    };
    $scope.randomBlock = function() {
        randomIndexSetValue([1]);
    };
    $scope.init = function() {
        $scope.cubics = [];
        for (var i = 1; i < $scope.cubicLen; i++) {
            $scope.cubics[i] = {
                textVal: ''
            }
            $scope.cubics[i].classVal = calClass($scope.cubics[i].textVal);
        }
        $scope.settingInfo.score = 0;

        if ($scope.settingInfo.blocked) {
            var num = $scope.settingInfo.blockedNumber;
            while (num > 0) {
                $scope.randomBlock();
                num--;
            }
        }
        $scope.randomCubic();
    };

    var calClass = function(val) {
        if (!val) {
            return 'class_common';
        } else if (val == 1) {
            return 'class_block';
        }
        return 'class_val_' + val.toString();
    };

    var checkEnd = function() {
        for (var i = 1; i < $scope.cubicLen; i++) {
            if ($scope.cubics[i].textVal == '' ||
            (i > parseInt($scope.settingInfo.scale) && $scope.cubics[i].textVal == $scope.cubics[i - parseInt($scope.settingInfo.scale)].textVal) ||
            (i > 1 && $scope.cubics[i].textVal == $scope.cubics[i - 1].textVal)) {
                return false;
            }
        }
        return true;

    };

    $scope.leftMove = function() {
        if (moveAction('L')) {
            $scope.randomCubic();
        }
        if (checkEnd()) {
            alert('end!');
        }
    };

    $scope.rightMove = function() {
        if (moveAction('R')) {
            $scope.randomCubic();
        }
        if (checkEnd()) {
            alert('end!');
        }

    };

    $scope.upMove = function() {
        if (moveAction('U')) {
            $scope.randomCubic();
        }
        if (checkEnd()) {
            alert('end!');
        }
    };

    $scope.downMove = function() {
        if (moveAction('D')) {
            $scope.randomCubic();
        }
        if (checkEnd()) {
            alert('end!');
        }
    };

    var getIndex = function(i, layer, direction, j) {
        var ret = {
            updatedIndex: 0,
            index: 0
        };
        var updatedR, R;
        switch (direction) {
        case 'U':
            updatedR = i - layer;
            R = updatedR + 1;
            ret.updatedIndex = parseInt($scope.settingInfo.scale) * (updatedR - 1) + j;
            ret.index = parseInt($scope.settingInfo.scale) * (R - 1) + j;
            break;
        case 'D':
            updatedR = parseInt($scope.settingInfo.scale) + 1 - i + layer;
            R = updatedR - 1;
            ret.updatedIndex = parseInt($scope.settingInfo.scale) * (updatedR - 1) + j;
            ret.index = parseInt($scope.settingInfo.scale) * (R - 1) + j;
            break;
        case 'L':
            updatedR = i - layer;
            R = updatedR + 1;
            ret.updatedIndex = updatedR + parseInt($scope.settingInfo.scale) * (j - 1);
            ret.index = R + parseInt($scope.settingInfo.scale) * (j - 1);
            break;
        case 'R':
            updatedR = parseInt($scope.settingInfo.scale) + 1 - i + layer;
            R = updatedR - 1;
            ret.updatedIndex = updatedR + parseInt($scope.settingInfo.scale) * (j - 1);
            ret.index = R + parseInt($scope.settingInfo.scale) * (j - 1);
            break;
        default:
            break;
        }
        return ret;
    };

    var moveAction = function(direction) {
        var moveFlag = false;
        var updatedFlag = [];
        for (var u = 1; u < $scope.cubicLen; u++) {
            updatedFlag[u] = true;
        }
        for (var i = 2; i < parseInt($scope.settingInfo.scale) + 1; i++) {
            for (var j = 1; j < parseInt($scope.settingInfo.scale) + 1; j++) {
                for (var l = 1; l < i; l++) {
                    var ret = getIndex(i, l, direction, j);
                    var updatedIndex = ret.updatedIndex;
                    var index = ret.index;
                    if ($scope.cubics[updatedIndex].textVal == 1 || $scope.cubics[index].textVal == 1) {
                        break;
                    }
                    if ($scope.cubics[updatedIndex].textVal == '' && $scope.cubics[index].textVal != '') {
                        $scope.cubics[updatedIndex].textVal = $scope.cubics[index].textVal;
                        $scope.cubics[index].textVal = '';
                        if (!updatedFlag[index]) {
                            updatedFlag[updatedIndex] = false;
                            updatedFlag[index] = true;
                        }
                        moveFlag = true;
                    } else if (updatedFlag[updatedIndex] && $scope.cubics[updatedIndex].textVal != '' && $scope.cubics[updatedIndex].textVal == $scope.cubics[index].textVal) {
                        $scope.cubics[updatedIndex].textVal += $scope.cubics[index].textVal;
                        $scope.settingInfo.score += $scope.cubics[index].textVal;
                        $scope.cubics[index].textVal = '';
                        moveFlag = true;
                        updatedFlag[updatedIndex] = false;
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        return moveFlag;
    };

    var randomIndexSetValue = function(values) {
        if (values) {
            $scope.availableIndex = [];
            for (var i = 1; i < $scope.cubicLen; i++) {
                if ($scope.cubics[i].textVal == '') {
                    $scope.availableIndex.push(i);
                }
            }
            if ($scope.availableIndex.length > 0) {
                var random = Math.floor(Math.random() * 10 * $scope.availableIndex.length);
                var index = random % $scope.availableIndex.length;
                var valuesIndex = 0;
                if (values.length > 1) {
                    var r = Math.floor(Math.random() * 10 * values.length);
                    valuesIndex = r % values.length;
                }
                $scope.cubics[$scope.availableIndex[index]].textVal = values[valuesIndex];
            }
        }
    };

    $scope.$watch("cubics", function() {
        if ($scope.cubics.length > 0) {
            for (var i = 1; i < $scope.cubicLen; i++) {
                $scope.cubics[i].classVal = calClass($scope.cubics[i].textVal);
            }
        }
    }, true);

    $scope.keydown = function(event) {
        if ($scope.cubics.length > 0) {
            if (event.which == 37) {
                $scope.leftMove();
            } else if (event.which == 38) {
                $scope.upMove();
            } else if (event.which == 39) {
                $scope.rightMove();
            } else if (event.which == 40) {
                $scope.downMove();
            }
        }
    };

    $scope.$on('destroy-validation-functor', function() {
        alert('destroy');
    });
}]);