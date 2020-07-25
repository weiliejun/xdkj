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
            field: 'serviceCode',
            width: 190,
            title: '类型编号',
            align: 'center',
            sort: true
        }, {
            field: 'serviceName',
            width: 190,
            title: '类型名称',
            align: 'center'
        }, {
            field: 'serviceModule',
            width: 120,
            title: '模块',
            align: 'center'
        }, {
            field: 'status',
            width: 100,
            title: '状态',
            align: 'center',
            templet: '#status'
        }, {
            field: 'serviceBrif',
            width: 200,
            title: '简介',
            align: 'center'
        }, {
            field: 'serviceLogo',
            width: 200,
            title: '服务logo',
            align: 'center'
        }, {
            field: 'docLink',
            width: 200,
            title: '文档链接',
            align: 'center'
        }, {
            field: 'createTime',
            width: 150,
            title: '创建时间',
            align: 'center',
            templet: '#createTime'
        }
        , {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#serviceInfoBar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#serviceInfoTables', '/serviceInfo/list', cols, table);

    //监听工具条
    table.on('tool(serviceInfoTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            var index = layui.layer.open({
                title: "编辑",
                type: 2,
                skin: '',
                offset: ['85px', '530px'],
                area: ['540px', '520px'],
                content: PageContext.getUrl("/serviceInfo/toadd"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find("#id").val(data.id);
                        body.find("#serviceCode").val(data.serviceCode);
                        body.find("#serviceName").val(data.serviceName);
                        body.find("#serviceModule").val(data.serviceModule);
                        body.find("#serviceBrif").val(data.serviceBrif);
                        body.find("#serviceDesc").val(data.serviceDesc);
                        body.find("#docLink").val(data.docLink);
                        //使用状态赋值
                        body.find("input[name=status]").each(function (i, item) {
                            if ($(item).val() == data.status) {
                                $(item).prop('checked', true);
                            }
                        });

                        form.render();
                    }
                }
            });
        } else if (obj.event === 'disable') {//禁用
            layer.confirm('确定要禁用此类型吗？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/serviceInfo/disable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('serviceInfoTables');
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
                    url: PageContext.getUrl('/serviceInfo/enable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('serviceInfoTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'delete') {
            layer.confirm('确定要删除该类型么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/serviceInfo/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('serviceInfoTables');
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
            Common.openFrame("/serviceInfo/toadd", "新增", '718px', '520px');
        }
    };
});