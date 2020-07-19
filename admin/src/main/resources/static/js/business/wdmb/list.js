layui.use(['layer', 'laydate', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table,
        laydate = layui.laydate;

    // 初始化日期选择器
    laydate.render({
        elem: '#rangeTime1', //指定元素
        range: true //开启日期范围，默认使用“_”分割
    });

    var cols = [[
        {
            field: 'attachFileName',
            title: '文档名称',
            align: 'center',
            width: '15%',
            sort: true
        }, {
            field: 'attachFilePath',
            title: '文档路径',
            align: 'center',
            width: '35%',
            sort: true
        }, {
            field: 'creatorName',
            title: '上传人',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'createTime',
            title: '上传时间',
            align: 'center',
            width: '15%',
            sort: true
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#wdmbBar',
            width: '25%',
            sort: true
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#wdmbTables', '/wdmb/list', cols, table);

    //监听工具条
    table.on('tool(wdmbTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            window.location.href = PageContext.getUrl("/wdmb/get/update/" + data.id);
        } else if (obj.event === 'view') {
            window.location.href = PageContext.getUrl("/wdmb/get/view/" + data.id);
        } else if (obj.event === 'downloadFile') {
            window.location.href = PageContext.getUrl("/wdmb/download?attachFileName=" + data.attachFileName + "&attachFilePath=" + data.attachFilePath);
        } else if (obj.event === 'delete') {
            layer.confirm('真的要删除吗？', function (index) {
                window.location.href = PageContext.getUrl("/wdmb/delete/" + data.id);
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
            //Common.openFrame("/app/wdmb/add", "新增人员", '1200px', '1000px');
            window.location.href = PageContext.getUrl("/wdmb/toAdd");
        }
    };

    //打开新窗口
    function addTab(_this) {
        tab.tabAdd(_this);
    }
});

