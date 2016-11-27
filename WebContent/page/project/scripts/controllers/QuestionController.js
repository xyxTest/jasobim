////////////////项目详情信息和项目基本信息页面切换
var index;
function QuestionController($scope,QuestionService) {
	console.log("载入QuestionController");
	var project="";
	var question="";
	var pageSize=10;
	var pageIndex=1;
	$scope.currentPage = 1;
	$scope.findQuestionInfo = {};
	$scope.questionTitles=["序号","问题类型","问题提交人","问题标题","专业","内容","问题创建时间","问题等级","问题状态","操作"];
	
	$scope.restStyle = function (a,b,c,d) {
		a ? document.getElementById("questionOfProject").style.display = 'none' :'';
		b ? document.getElementById("questionOfType").style.display = 'none' : '';
		c ? document.getElementById("questionOfPority").style.display = 'none': '';
		d ? document.getElementById("questionOfStatu").style.display = 'none': '';
	}
	$scope.showProjects=function(){
		$scope.getProjectLists(pageSize,pageIndex,project);
		$scope.restStyle(false,true,true,true);
		var idom = document.getElementById("questionOfProject");
		console.log(idom.style.display);
		if(idom.style.display == 'none') {
			idom.style.display = 'block';
		} else {
			idom.style.display = 'none';
		}
	}
	
	
	$scope.showQuestionType=function(){
		$scope.restStyle(true,false,true,true);
		var idom = document.getElementById("questionOfType");
		if(idom.style.display == 'none') {
			idom.style.display = 'block';
		} else {
			idom.style.display = 'none';
		}
	}
	$scope.showQuestionPority=function (){
		$scope.restStyle(true,true,false,true);
		var idom = document.getElementById("questionOfPority");
		if(idom.style.display == 'none') {
			idom.style.display = 'block';
		} else {
			idom.style.display = 'none';
		}
	}
	$scope.showQuestionStatu=function (){
		$scope.restStyle(true,true,true,false);
		var idom = document.getElementById("questionOfStatu");
		if(idom.style.display == 'none') {
			idom.style.display = 'block';
		} else {
			idom.style.display = 'none';
		}
	}
	 ////////问题分页回调函数
	  $scope.questionPage = function(iPageCount,iCurrent) {
		  $("#questionsPageCode").remove();
		  $("#table-buton8").append("<div id=\"questionsPageCode\"></div>");
		  $("#questionsPageCode").createPage({

		      pageCount:iPageCount,

		      current:iCurrent,

		      backFn:function(p){
		    	  $scope.getQuestionList(pageSize,p,question);
		      }
		  });
	  }
	 ////////////////////////问题列表分页获取
	 $scope.getQuestionList = function(pageSize,pageIndex,question) {
		  QuestionService.getQuestionList(pageSize,pageIndex,question).then(function (result){
		  	  $scope.questionList = result.data;
		      $scope.currentPage = result.currentPage;
		      $scope.totalPage = result.totalPage;
		      $scope.questionPage($scope.totalPage,$scope.currentPage);
		  });
	  }
	 /////初始化获取问题列表
	 $scope.getQuestionList(pageSize,pageIndex,question);
	 ////////问题重置
	 $scope.resetQuestion = function(){
		 $scope.findQuestionInfo={};
	 }
	 /////增加问题
	 $scope.addQuestionByAdmin = function(){
		 
			 var formData = new FormData();
			 for (var key in $scope.questionList) {
				   if($scope.questionList[key] != null) {
					   formData.append(key, $scope.findProjectInfo[key]);
				   }
			 }
			 
			 ProjectService.addQuestionByAdmin(formData).then(function(result){
			       $scope.questionInfo=result.data; 
			       $scope.questionid=result.data.id;
			       $scope.getQuestionList(pageSize,1,question);
			      
			    });
		 
		 
		 
	 }
	 ////显示问题详细界面
	 $scope.questionChangeClick = function(questionId){
		    QuestionService.findQuestion(questionId).then(function(result){
		      $scope.findQuestionInfo=result.data;
		      document.getElementById("addQuestionHtml").style.display = 'block';
			  document.getElementById("projectDetail_body_questions").style.display='none';
		    });
		 }
	 /////显示增加界面
	 $scope.showAddQuestionHtml = function(){
		 $scope.findQuestionInfo={};
		 document.getElementById("addQuestionHtml").style.display = 'block';
	     document.getElementById("projectDetail_body_questions").style.display='none';
	 }
	 /////返回问题列表
	 $scope.returnQuestionlist = function(){
		 $scope.findQuestionInfo={};
		 document.getElementById("addQuestionHtml").style.display = 'none';
	     document.getElementById("projectDetail_body_questions").style.display='block';
	     $scope.getQuestionList(pageSize,pageIndex,question);
	 }
	 ////隐藏问题添加页面
	 $scope.hideAddQuestionHtml = function(){
		 $scope.findQuestionInfo={};
		 document.getElementById("addQuestionHtml").style.display = 'none';
	     document.getElementById("projectDetail_body_questions").style.display='block';
	 }
	 ///////分页获取项目列表
	$scope.getProjectLists = function(pageSize,pageIndex,project) {
		  QuestionService.getProjectLists(pageSize,pageIndex,project).then(function (result){
		  	  $scope.projectLists = result.data;
		   /*   $scope.currentPage = result.currentPage;
		      $scope.totalPage = result.totalPage;
		      $scope.projectPage($scope.totalPage,$scope.currentPage);*/
		  });
	  }
}