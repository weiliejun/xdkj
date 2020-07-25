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
            field: 'SERVICE_NAME',
            width: 250,
            title: '服务名称',
            align: 'center',
            sort: true
        }, {
            field: 'CUSTOMER_NAME',
            width: 220,
            title: '用户名',
            align: 'center',
            sort: true
        }, {
            field: 'CUSTOMER_PHONE',
            width: 220,
            title: '手机号',
            align: 'center',
            sort: true
        }, {
            title: '联系ta',
            width: 200,
            align: 'center',
            fixed: "right",
            toolbar: '#saasApplyBar'
        }, {
            field: 'CREATE_TIME',
            width: 300,
            title: '注册时间',
            align: 'center',
            sort: true,
            templet: '#createTime'
        }
    ]];
    // 表格渲染
    var initTable = Common.initTable('#saasApplyTables', '/serviceApply/saas/list', cols, table);


    //监听工具条
    table.on('tool(saasApplyTables)', function (obj) {
        console.log(obj);
        var data = obj.data;
        if (obj.event === 'undo') {//禁用
            layer.confirm('确定要设置为已联系吗？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/serviceApply/saas/resetStatus'),
                    type: 'post',
                    async: false,
                    data: {
                        id: data.ID,
                        operateType: 'undo'
                    },
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('saasApplyTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
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