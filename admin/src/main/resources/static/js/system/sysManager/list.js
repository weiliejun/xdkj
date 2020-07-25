layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols = [[
        {
            checkbox: true,
            width: 60,
            fixed: true
        }, {
            field: 'code',
            width: 220,
            title: '用户名',
            align: 'center',
            sort: true
        }, {
            field: 'name',
            width: 220,
            title: '真实姓名',
            align: 'center'
        }, {
            field: 'email',
            width: 255,
            title: '邮箱',
            align: 'center'
        }, {
            field: 'mobile',
            width: 200,
            title: '手机号',
            align: 'center'
        }, {
            field: 'status',
            width: 150,
            title: '状态',
            align: 'center',
            templet: '#status'
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#sysManagerbar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#sysManagerTables', '/sysmanager/list', cols, table);

    //监听工具条
    table.on('tool(sysManagerTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            var index = layui.layer.open({
                title: "修改用户",
                type: 2,
                skin: '',
                offset: ['85px', '530px'],
                area: ['540px', '520px'],
                content: PageContext.getUrl("/sysmanager/toadd"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find("#id").val(data.id);
                        body.find("#code").val(data.code);
                        body.find("#name").val(data.name);
                        body.find("#email").val(data.email);
                        body.find("#mobile").val(data.mobile);
                        body.find("#initPasswordTip").hide();
                        form.render();
                    }
                }
            });
        } else if (obj.event === 'roleSetting') {
            var index = layui.layer.open({
                title: "设置用户角色",
                area: ['1010px', '565px'],
                type: 2,
                skin: 'layui-layer-molv',
                content: PageContext.getUrl("/sysmanager/role/setting?managerId=" + data.id)
            });
        } else if (obj.event === 'disable') {//禁用
            layer.confirm('真的禁用用户么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysmanager/disable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysManagerTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'enable') {//启用
            layer.confirm('真的将该用户置为可用么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysmanager/enable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysManagerTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'unlock') {//解锁
            layer.confirm('真的将该用户解锁么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysmanager/unlock'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysManagerTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除该用户及其关联数据么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysmanager/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysManagerTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('删除失败', {icon: 5});
                }
                layer.close(index);
            });
        }
    });

    //按钮事件监听
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //按钮事件定义
    var active = {
        search: function () {
            Common.searchTable('searchForm', initTable);
        },
        searchFormClear: function () {
            Common.searchTableClear('searchForm');
        },
        add: function () {
            Common.openFrame("/sysmanager/toadd", "新增用户", '540px', '520px');
        }
    };
});