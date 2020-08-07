layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols = [[
        {
            field: 'topic',
            width: 220,
            title: '公告标题',
            align: 'center',
            sort: true
        }, {
            field: 'content',
            width: 255,
            title: '内容',
            align: 'center',
            sort: true
        }, {
            field: 'clicks',
            width: 100,
            title: '点击次数',
            align: 'center',
            sort: true
        }, {
            field: 'status',
            width: 150,
            title: '是否可用',
            align: 'center',
            templet: '#status',
            sort: true
        }, {
            field: 'createTime',
            width: 250,
            title: '创建时间',
            align: 'center',
            templet: '#createTime',
            sort: true
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#websiteBulletinbar',
            sort: true
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#websiteBulletinTables', '/websiteBulletin/list', cols, table);

    //监听工具条
    table.on('tool(websiteBulletinTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            // window.location.href = PageContext.getUrl("/websiteBulletin/system/toAdd?id=" + data.id);
            Common.openFrame("/websiteBulletin/system/toAdd?id=" + data.id, "修改公告", '800px', '600px');
            /*var index = layui.layer.open({
                title: "修改公告",
                type: 2,
                skin: '',
                // offset: ['85px', '330px'],
                area: ['800px', '700px'],
                content: PageContext.getUrl("/websiteBulletin/toadd"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find("#id").val(data.id);
                        body.find("#topic").val(data.topic);
                        body.find("#type").val(data.type);
                        body.find("#content").val(data.content);
                        //公告类型赋值
                        body.find("input[name=type]").each(function (i, item) {
                            if ($(item).val() == data.type) {
                                $(item).prop('checked', true);
                            }
                        });
                        //发布状态赋值
                        body.find("input[name=publishStatus]").each(function (i, item) {
                            if ($(item).val() == data.publishStatus) {
                                $(item).prop('checked', true);
                            }
                        });
                        //是否置顶赋值
                        body.find("input[name=topMark]").each(function (i, item) {
                            if ($(item).val() == data.topMark) {
                                $(item).prop('checked', true);
                            }
                        });
                        form.render();
                    }
                }
            });*/
        } else if (obj.event === 'disable') {//禁用
            layer.confirm('真的禁用公告么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/websiteBulletin/disable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('websiteBulletinTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'enable') {//启用
            layer.confirm('真的将该公告置为可用么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/websiteBulletin/enable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('websiteBulletinTables');
                    layer.msg(ajaxReturnData.msg, {icon: 1});
                } else {
                    layer.msg('操作失败', {icon: 5});
                }
                layer.close(index);
            });
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除该公告么？', function (index) {
                var ajaxReturnData;
                $.ajax({
                    url: PageContext.getUrl('/websiteBulletin/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('websiteBulletinTables');
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
            Common.openFrame("/websiteBulletin/system/toAdd", "新增公告", '800px', '600px');
        }
    };
});