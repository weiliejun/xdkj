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
            title: '类型编号',
            align: 'center',
            sort: true
        }, {
            field: 'name',
            width: 220,
            title: '类型名称',
            align: 'center'
        }, {
            field: 'parentCode',
            width: 255,
            title: '父级编号',
            align: 'center'
        }, {
            field: 'status',
            width: 150,
            title: '状态',
            align: 'center',
            templet: '#status'
        }, {
            field: 'createTime',
            width: 200,
            title: '创建时间',
            align: 'center',
            templet: '#createTime'
        }
        , {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#dictionaryBar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#dictionaryTables', '/sysdictionary/list', cols, table);

    //监听工具条
    table.on('tool(dictionaryTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            var index = layui.layer.open({
                title: "编辑",
                type: 2,
                skin: '',
                offset: ['85px', '530px'],
                area: ['540px', '520px'],
                content: PageContext.getUrl("/sysdictionary/toadd"),
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    console.log(body.find("#parentCodeDiv"))
                    if (data) {
                        body.find("#id").val(data.id);
                        body.find("#code").val(data.code);
                        body.find("#name").val(data.name);
                        //级别类型
                        body.find("#grade").val(data.grade);
                        body.find("#sort").val(data.sort);
                        // 级别为子级2，显示父级类型编号
                        if (data.grade == '2') {
                            body.find("#parentCodeDiv").removeClass("layui-hide");
                            body.find("#parentCodeDiv").attr("lay-verify", "required");
                            body.find("#parentCode").val(data.parentCode);
                        }
                        //是否有子节点赋值
                        body.find("input[name=hasChild]").each(function (i, item) {
                            if ($(item).val() == data.hasChild) {
                                $(item).prop('checked', true);
                            }
                        });
                        //使用状态赋值
                        body.find("input[name=status]").each(function (i, item) {
                            if ($(item).val() == data.status) {
                                $(item).prop('checked', true);
                            }
                        });

                        // 级别下拉框-选择事件(选择父级1时，隐藏类型下拉框/去掉必填)
                        form.on('select(grade)', function (data) {
                            var selectVal = data.value;
                            alert(typeof(selectVal));
                            if (selectVal == '1') {
                                body.find("#parentCodeDiv").addClass("layui-hide");
                                body.find("#parentCodeDiv").removeClass("lay-verify", "required");
                            } else if (selectVal == '2') {
                                body.find("#parentCodeDiv").removeClass('layui-hide');
                                body.find("#parentCodeDiv").attr("lay-verify");
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
                    url: PageContext.getUrl('/sysdictionary/disable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('dictionaryTables');
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
                    url: PageContext.getUrl('/sysdictionary/enable'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                if (ajaxReturnData.flag == 'true') {
                    table.reload('dictionaryTables');
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
                    url: PageContext.getUrl('/sysdictionary/delete'),
                    type: 'post',
                    async: false,
                    data: {id: data.id},
                    success: function (data) {
                        ajaxReturnData = data;
                    }
                });
                //删除结果
                if (ajaxReturnData.flag == 'true') {
                    table.reload('dictionaryTables');
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
            Common.openFrame("/sysdictionary/toadd", "新增", '712px', '520px');
        }
    };
});