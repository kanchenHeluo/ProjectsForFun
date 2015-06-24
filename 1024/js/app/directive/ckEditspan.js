testApp.directive('ckEditspan', function(){
	return {
		restrict: 'E',
		scope: {
			ngModel: '='
		},
		template: '<span id="editspan">{{ngModel}}</span>',
		link: function(){
			$('#editspan').click(function(){
				$(this).attr('contentEditable', true);
			});
			$('#editspan').blur(function(){
				$(this).attr('contentEditable', false);
			});
			$('#editspan').hover(function(){
				this.style.cursor = 'text';
			});
		}
	}
});