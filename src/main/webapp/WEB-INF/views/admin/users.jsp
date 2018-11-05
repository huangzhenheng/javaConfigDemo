<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html>
<head>
    <title>CRM|员工管理</title>
	<%@ include file="../include/includeCSS.jsp" %>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <%@ include file="../include/mainHeader.jsp" %>
    <jsp:include page="../include/leftSide.jsp">
        <jsp:param name="menu" value="employee"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-12">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">用户列表</h3>
                            <a href="/admin/export" class="btn btn-xs btn-success pull-right"><i
                                    class="fa fa-arrow-down"></i> 导出用户</a>
                            <span class="pull-right">&nbsp;&nbsp;</span>
                            <a href="javascript:;" class="btn btn-xs btn-success pull-right" id="importBtn"><i
                                    class="fa fa-arrow-up"></i> 批量添加</a>
                            <span class="pull-right">&nbsp;&nbsp;</span>
                            <a href="javascript:;" class="btn btn-xs btn-success pull-right" id="saveBtn"><i
                                    class="fa fa-user-plus"></i> 新增用户</a>
                        </div>
                        <div class="box-body">

                            <table id="dataTable" class="table table table-bordered">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>用户名</th>
                                    <th>真实姓名</th>
                                    <th>微信号</th>
                                    <th>身份</th>
                                    <th>状态</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- Control Sidebar -->
</div>
<!-- Modal ,添加新用户-->
<div class="modal fade" id="newUserModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">添加新用户</h4>
            </div>
            <div class="modal-body">
                <form id="newUserForm" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">用户名</label>
                        <div class="col-sm-5">
                            <input type="text" autofocus class="form-control" placeholder="请输入用户名" name="username">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">默认密码</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" placeholder="请输入密码" name="password" value="000000">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">真实姓名</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" placeholder="请输入真实姓名" name="realname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">微信号</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" placeholder="请输入微信号" name="weixin">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">身份</label>
                        <div class="col-sm-5">
                            <select class="form-control" name="roleid">
                                <option value="">-请选择类别-</option>
                                <c:forEach items="${role}" var="rol">
                                    <option value="${rol.id}">${rol.rolename}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">状态</label>
                        <div class="col-sm-5">
                            <select class="form-control" name="status" id="status">
                                <option value="">-用户状态-</option>
                                <option value="1">正常</option>
                                <option value="0">禁用</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="saveBt">添加</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal ,修改用户-->
<div class="modal fade" id="editUserModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">修改用户</h4>
            </div>
            <div class="modal-body">
                <form id="editBookForm" class="form-horizontal">
                    <input type="hidden" name="id" id="e_userid">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">用户名</label>
                        <div class="col-sm-5">
                            <input type="text" disabled class="form-control" placeholder="请输入用户名" id="e_username"
                                   name="username">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">真实姓名</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" placeholder="请输入真实姓名" id="e_realname"
                                   name="realname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">微信号</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" placeholder="请输入微信号" id="e_weixin" name="weixin">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">身份</label>
                        <div class="col-sm-5">
                            <select class="form-control" name="roleid" id="e_roleid">
                                <option value="">-请选择类别-</option>
                                <c:forEach items="${role}" var="rol">
                                    <option value="${rol.id}">${rol.rolename}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">状态</label>
                        <div class="col-sm-5">
                            <select class="form-control" name="status" id="e_status">
                                <option value="true">正常</option>
                                <option value="false">禁用</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="editBtn">修改</button>
            </div>
        </div>
    </div>
</div>




<!-- Modal ,批量导入-->
<div class="modal fade" id="importUsers">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">批量添加用户</h4>
            </div>
            <div class="modal-body">
                <form id="users" action="/admin/import" method="post" class="form-horizontal"  enctype="multipart/form-data">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">请选择Excel文件</label>
                        <div class="col-sm-5">
                            <input type="file" class="form-control" name="file">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="saveUsers">添加</button>
            </div>
        </div>
    </div>
</div>

<%@ include file="../include/includeJS.jsp"%>
<script>
    $(function () {

        var dataTable = $("#dataTable").DataTable({
            "lengthMenu": [5, 10, 25, 50],
            "ordering": false,
            "serverSide": true,
            "autoWidth": false,
            "ajax": "/admin/data.json",
            "columns": [ //配置返回的JSON中[data]属性中数据key和表格列的对应关系
                {"data": "id"},
                {"data": "username"},
                {"data": "realname"},
                {"data": "weixin"},
                {"data": "role.rolename"},
                {
                    "data": function (row) {
                        if (row.status == 1) {
                            return "<span class='label label-success'>正常</span>";
                        } else {
                            return "<span class='label label-danger'>禁用</span>";
                        }
                    }
                },
                {"data":function(row){
                    var timestamp = row.createtime;
                    var day = moment(timestamp);
                    return day.format("YYYY-MM-DD HH:mm");
                }},
                {
                    "data": function (row) {
//                     	if (row.username != "king") {
                        if (row.username != "") {
                            return '<a href="javascript:;" rel="' + row.id + '" class="btn btn-success btn-xs editLink">修改</a>&nbsp;<a href="javascript:;" rel="' + row.id + '" class="btn btn-info btn-xs resetLink">密码重置</a>';
                        }else {
                            return "";
                        }
                    }
                }
            ],
            "columnDefs": [ //定义列的特征
                {targets: [0], visible: false}
            ],
            "language": { //定义中文
                "search": "搜索:",
                "zeroRecords": "没有匹配的数据",
                "lengthMenu": "显示 _MENU_ 条数据",
                "info": "第 _START_ 条到第 _END_ 条，共 _TOTAL_ 条数据",
                "infoFiltered": "(源数据共 _MAX_ 条)",
                "loadingRecords": "加载中...",
                "processing": "处理中...",
                "paginate": {
                    "first": "首页",
                    "last": "末页",
                    "next": "下一页",
                    "previous": "上一页"
                }
            }
        });

        $("#saveBtn").click(function () {
            $("#newUserForm")[0].reset(); //让表单进行重置
            $("#newUserModal").modal({
                show: true,
                backdrop: 'static',
                keyboard: false
            });
        });
        $("#saveBt").click(function () {
            $("#newUserForm").submit();
        });
        $("#newUserForm").validate({
            errorElement: 'span',
            errorClass: 'text-danger',
            rules: {
                username: {
                    required: true,
                    rangelength: [3, 18],
                    remote: "/admin/checkuser"
                },
                password: {
                    required: true,
                    rangelength: [6, 18]
                },
                realname: {
                    required: true,
                    rangelength: [2, 18]
                },
                weixin: {
                    required: true
                }
            },
            messages: {
                username: {
                    required: "*请输入用户名",
                    rangelength: "*用户名长度6~18位",
                    remote: "*该用户名已被占用"
                },
                password: {
                    required: "*请输入用密码",
                    rangelength: "*密码长度6~18位"
                },
                realname: {
                    required: "*请真实姓名",
                    rangelength: "*真实姓名长度6~18位"
                },
                weixin: {
                    required: "*请输入微信号"
                }
            },
            submitHandler: function (form) {
                $.post("/admin/new", $(form).serialize()).done(function (data) {
                    if (data == "success") {
                        $("#newUserModal").modal('hide');
                        dataTable.ajax.reload();
                    }
                }).fail(function () {
                    alert("服务器请求失败！");
                });
            }
        });

        $(document).delegate(".editLink", "click", function (date) {
            var id = $(this).attr("rel");
            $.get("/admin/" + id + ".json").done(function (result) {
                //将JSON数据显示在表单上
                if(result.state == "success"){
                    $("#e_userid").val(result.data.id);
                    $("#e_username").val(result.data.username);
                    $("#e_realname").val(result.data.realname);
                    $("#e_weixin").val(result.data.weixin);
                    $("#e_roleid").val(result.data.roleid);
                    $("#e_status").val(result.data.status.toString());
                    $("#editUserModal").modal({
                        show: true,
                        backdrop: 'static',
                        keyboard: false
                    });

                }else {}

            }).fail(function () {
                alert("服务器请求异常！");
            });
        });
        $("#editBtn").click(function () {
            $("#editBookForm").submit();
        });

        $("#editBookForm").validate({
            errorElement: 'span',
            errorClass: 'text-danger',
            rules: {
                username: {
                    required: true,
                    rangelength: [3, 18]
                },
                realname: {
                    required: true,
                    rangelength: [2, 18]
                },
                weixin: {
                    required: true
                }
            },
            messages: {
                username: {
                    required: "*请输入用户名",
                    rangelength: "*用户名长度6~18位"
                },
                realname: {
                    required: "*请真实姓名",
                    rangelength: "*真实姓名长度6~18位"
                },
                weixin: {
                    required: "*请输入微信号"
                }
            },
            submitHandler: function (form) {
                $.post("/admin/edit", $(form).serialize()).done(function (data) {
                    if (data == "success") {
                        $("#editUserModal").modal('hide');
                        dataTable.ajax.reload();
                    }
                }).fail(function () {
                    alert("服务器请求失败！");
                });
            }
        });

        $(document).delegate(".resetLink", "click", function () {

            if (confirm("是否确定重置密码为：000000？")) {
                $.get("/admin/" + $(this).attr("rel") + "/resetpassword").done(function (data) {
                    if (data == "success") {
                        alert("密码重置成功！");
                    }
                }).fail(function () {
                    alert("服务器请求异常！");
                });

            }
        });


        $("#importBtn").click(function () {
            $("#importUsers").modal({
                show: true,
                backdrop: 'static',
                keyboard: false
            });
        });
        $("#saveUsers").click(function () {
            $("#users").submit();
        });
    });
</script>
</body>
</html>
