$(function(){
	$(".msg_tab .tab a").click(function(){
		$(this).addClass('on').siblings().removeClass('on');
		var index = $(this).index();
		number = index;
		$('.msg_tab .content li').hide();
		$('.msg_tab .content li:eq('+index+')').show();
		console.log("type:"+$(this).attr("type"));
	});
	
	var auto = 1;  //等于1则自动切换，其他任意数字则不自动切换
	if(auto ==1){
		var number = 0;
		var maxNumber = $('.msg_tab .tab a').length;
		function autotab(){
			number++;
			number == maxNumber? number = 0 : number;
			$('.msg_tab .tab a:eq('+number+')').addClass('on').siblings().removeClass('on');
			$('.msg_tab .content ul li:eq('+number+')').show().siblings().hide();
		}
		//var tabChange = setInterval(autotab,3000);
		//鼠标悬停暂停切换
		$('.msg_tab').mouseover(function(){
			//clearInterval(tabChange);
		});
		$('.msg_tab').mouseout(function(){
			//tabChange = setInterval(autotab,3000);
		});
	  }

});