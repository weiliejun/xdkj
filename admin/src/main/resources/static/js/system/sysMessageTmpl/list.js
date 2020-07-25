layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        table = layui.table,
        form = layui.form;


    var cols = [[
        {
            field: 'parentCode',
            width: 200,
            title: '父级类型',
            align: 'center'
        }, {
            field: 'busiType',
            width: 200,
            title: '业务类型',
            align: 'center'
        }, {
            field: 'type',
            width: 150,
            title: '消息发送类型',
            align: 'center'
        }, {
            field: 'title',
            width: 200,
            title: '标题',
            align: 'center'
        }, {
            field: 'content',
            width: 200,
            title: '内容',
            align: 'center',
            templet: '#content'
        }, {
            field: 'status',
            width: 150,
            title: '状态',
            align: 'center',
            templet: '#status'
        }, {
            field: 'createTime',
            width: 250,
            title: '时间',
            align: 'center',
            templet: '#createTime'
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#sysMessageTmplBar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#sysMessageTmplTables', '/sysMessageTmpl/list', cols, table);

    //监听工具条
    table.on('tool(sysMessageTmplTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            var index = layui.layer.open({
                title: "修改消息模板",
                type: 2,
                skin: 'layui-layer-molv',
                offset: ['85px', '530px'],
                area: ['500px', '550px'],
                content: "/sysMessageTmpl/toAdd",
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find("select[name=parentCode]").find("option[value='" + data.parentCode + "']").prop("selected", true);
                        body.find("select[name=busiType]").find("option[value='" + data.busiType + "']").prop("selected", true);
                        body.find("#title").val(data.title);
                        body.find("#content").val(data.content);
                        form.render();
                    }
                }
            });
        } else if (obj.event === 'disable') {
            // 禁用
            Common.confirmFrame('/sysMessageTmpl/disable', '真的禁用该模板么', {id: data.id}, 'sysMessageTmplTables', table);
        } else if (obj.event === 'enable') {
            // 启用
            Common.confirmFrame('/sysMessageTmpl/enable', '真的将该模板置为可用么', {id: data.id}, 'sysMessageTmplTables', table);
        } else if (obj.event === 'delete') {
            // 删除
            Common.confirmFrame('/sysMessageTmpl/delete', '真的删除该模板么', {id: data.id}, 'sysMessageTmplTables', table);
        } else if (obj.event === 'detail') {
            // 查看消息内容
            layer.alert(data.content, {
                title: '内容',
                skin: 'layui-layer-molv', //样式类名
                closeBtn: 0
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
            Common.openFrame("/sysMessageTmpl/toAdd", "新增模板", '500px', '550px');
        }
    };
});
