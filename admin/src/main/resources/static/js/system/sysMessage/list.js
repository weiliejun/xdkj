layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols = [[
        {
            field: 'topic',
            width: '10%',
            title: '标题',
            align: 'center',
            sort: true
        }, {
            field: 'content',
            width: '60%',
            hight: 500,
            title: '内容',
            align: 'center',
            sort: true
        }, {
            field: 'status',
            width: '10%',
            title: '状态',
            align: 'center',
            templet: '#status',
            sort: true
        }, {
            field: 'createTime',
            width: '10%',
            title: '时间',
            align: 'center',
            templet: '#createTime',
            sort: true
        }, {
            title: '常用操作',
            width: '10%',
            align: 'center',
            fixed: "right",
            toolbar: '#sysMessagebar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#sysMessageTables', '/sysMessage/list?cxmk='+$("#cxmk").val(), cols, table);

    //监听工具条
    table.on('tool(sysMessageTables)', function (obj) {
        var data = obj.data;
        if (obj.event === 'delete') {
            layer.confirm('真的删除该消息么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysMessage/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysMessageTables');
                    document.location.reload();
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