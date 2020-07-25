layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols = [
        [{
            checkbox: true,
            width: 60,
            fixed: true
        }, {
            field: 'name',
            width: 300,
            title: '角色名称',
            align: 'center',
            sort: true
        }, {
            field: 'description',
            width: 255,
            title: '描述',
            align: 'center'
        }, {
            field: 'status',
            width: 150,
            title: '状态',
            align: 'center',
            templet: '#status'
        }, {
            field: 'createTime',
            width: 300,
            title: '创建时间',
            align: 'center'
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#sysManagerbar'
        }]
    ];


    // 表格渲染
    var initTable = Common.initTable('#sysRoleTables', '/sysrole/list', cols, table);

    //监听工具条
    table.on('tool(sysRoleTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            var index = layui.layer.open({
                title: "修改角色",
                type: 2,
                skin: 'layui-layer-molv',
                offset: ['85px', '530px'],
                area: ['500px', '450px'],
                content: PageContext.getUrl("/sysrole/toadd"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find("#id").val(data.id);
                        body.find("#name").val(data.name);
                        body.find("#description").val(data.description);
                        //有效性赋值
                        body.find("input[name=status]").each(function (i, item) {
                            if ($(item).val() == data.status) {
                                $(item).prop('checked', true);
                            }
                        });
                        form.render();
                    }
                }
            });
        } else if (obj.event === 'grantRoleRights') {//设置菜单权限
            var index = layui.layer.open({
                title: "设置菜单权限",
                area: ['263px', '490px'],
                type: 2,
                skin: 'layui-layer-molv',
                content: PageContext.getUrl("/sysrole/grant/rights"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find("#roleId").val(data.id);
                        var iframeWin = window[layero.find('iframe')[0]['name']];
                        iframeWin.treeInit();
                    }
                }
            });
        } else if (obj.event === 'disable') {//禁用
            layer.confirm('真的禁用该角色么', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysrole/disable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysRoleTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'enable') {//启用
            layer.confirm('真的将该角色置为可用么', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysrole/enable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysRoleTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除该角色及其关联数据么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysrole/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysRoleTables');
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
            Common.openFrame("/sysrole/toadd", "新增用户", '500px', '450px');
        }
    };
});