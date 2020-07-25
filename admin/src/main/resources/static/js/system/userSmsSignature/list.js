layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols = [[
        {
            field: 'signature',
            width: 220,
            title: '短信签名',
            align: 'center',
            sort: true
        }, {
            field: 'mobile',
            width: 255,
            title: '手机号',
            align: 'center'
        }, {
            field: 'reviewStatus',
            width: 255,
            title: '状态',
            align: 'center',
            templet: '#reviewStatus'
        }, {
            field: 'createTime',
            width: 350,
            title: '创建时间',
            align: 'center',
            templet: '#createTime'
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#userSmsSignaturebar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#userSmsSignatureTables', '/userSmsSignature/list', cols, table);

    //监听工具条
    table.on('tool(userSmsSignatureTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            var index = layui.layer.open({
                title: "审核签名",
                type: 2,
                skin: '',
                offset: ['85px', '530px'],
                area: ['540px', '520px'],
                content: PageContext.getUrl("/userSmsSignature/toadd"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find("#id").val(data.id);
                        body.find("#mobile").val(data.mobile);
                        body.find("#signature").val(data.signature);
                        //有效性赋值
                        body.find("input[name=reviewStatus]").each(function (i, item) {
                            if ($(item).val() == data.reviewStatus) {
                                $(item).prop('checked', true);
                            }
                        });
                        form.render();
                    }
                }
            });
        } else if (obj.event === 'disable') {//禁用
            layer.confirm('真的拒绝该短信签名么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/userSmsSignature/disable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('userSmsSignatureTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'enable') {//通过
            layer.confirm('真的通过该短信签名么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/userSmsSignature/enable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('userSmsSignatureTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除该短信签名么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/userSmsSignature/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('userSmsSignatureTables');
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
        }
    };
});