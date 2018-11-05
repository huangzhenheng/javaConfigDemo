<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>CRM|主页</title>
  	<%@ include file="include/includeCSS.jsp" %>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="include/mainHeader.jsp"%>
    <jsp:include page="include/leftSide.jsp">
        <jsp:param name="menu" value="home"/>
    </jsp:include>


    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" style="font-family: 宋体">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <div id="weatherBox"> </div>
            <div class="box-tools pull-right">
              	<span id="picker"><span class="text btn btn-xs btn-success"><i class="fa fa-upload"></i> 上传文件</span></span>
        	</div>
        </section>

        <!-- Main content -->
        <section class="content">

           <div class="container">
			    <div class="page-header">
			    	<h3>欢迎登录 </h3>
			        <h3>电子词典</h3>
			       
			    </div>
			    <div class="form-group">
			    <form id="hzhForm" action="/captcha">
			      	<input type="text" id="keyword" name="hzh" class="col-sm-4 " placeholder="请输入要翻译单词">
			        <button id="btn">翻译</button>
			    </form>
			      
			    </div>
    			<p id="result"></p>
				<img id="captcha" alt="" src="/p.png">
			</div>
  
            
        </section>
        <!-- /.content -->
    </div>
</div>

<%@ include file="include/includeJS.jsp" %>
<script>

$(function () {
	$("#captcha").click(function(){
		$(this).attr("src","/p.png?date="+new Date()); 
	});
	
	$("#btn").click(function(){
		$("#hzhForm").submit(); 
	});
});
</script>
</body>
</html>
