$("#btn_publish").click(function(){
	var topic_name = $("#topic_name").val();
	var topic_content = $("#topic_content").val();
	if(topic_name==""||topic_content=="")
	{
		alert("话题　及　话题描述不能为空");
		return;
	}
	$("#form1").submit();
});