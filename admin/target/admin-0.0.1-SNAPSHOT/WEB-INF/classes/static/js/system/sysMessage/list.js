layui.use(['layer', 'form', 'table'], function() {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols =  [[
        {
            field: 'topic',
            width: 220,
            title: '名称',
            align: 'center'
        },{
            field: 'code',
            width: 200,
            title: '手机号验证码',
            align: 'center'
        },{
            field: 'mobile',
            width: 200,
            title: '手机号',
            align: 'center'
        },{
            field: 'content',
            width: 280,
            title: '内容',
            align: 'center'
        }, {
            field: 'status',
            width: 255,
            title: '状态',
            align: 'center',
            templet:'#status'
        }, {
            field: 'createTime',
            width: 250,
            title: '时间',
            align: 'center',
            templet:'#createTime'
        },{
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#sysMessagebar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#sysMessageTables', '/sysMessage/list',cols,table);

    //监听工具条
    table.on('tool(sysMessageTables)', function(obj) {
        var data = obj.data;
        if (obj.event === 'delete') {
            layer.confirm('真的删除该消息么？', function(index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/sysMessage/delete'),
                    type: 'post',
                    async: false,
                    data: {id:data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('sysMessageTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('删除失败', {icon: 5});
                }
                layer.close(index);
            });
        }
    });

    //按钮事件监听
    $('.layui-btn').on('click',function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //按钮事件定义
    var active = {
        search:function(){
            Common.searchTable('searchForm', initTable);
        },
        searchFormClear:function(){
            Common.searchTableClear('searchForm');
        }
    };
});