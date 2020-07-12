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
            field: 'pp',
            title: '品牌',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'sblb',
            title: '设备类别',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'cpdm',
            title: '产品代码',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'csms',
            title: '参数描述',
            align: 'center',
            width: '20%',
            sort: true
        }, {
            field: 'dw',
            title: '单位',
            align: 'center',
            width: '5%',
            sort: true
        }, {
            field: 'sl',
            title: '数量',
            align: 'center',
            width: '5%',
            sort: true
        }, {
            field: 'znjg',
            title: '租赁价格',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'wbjg',
            title: '维保价格',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#deviceInfoBar',
            width: '20%',
            sort: true
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#deviceInfoTables', '/deviceInfo/list', cols, table);

    //监听工具条
    table.on('tool(deviceInfoTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            window.location.href = PageContext.getUrl("/deviceInfo/get/update/" + data.id);
        } else if (obj.event === 'view') {
            window.location.href = PageContext.getUrl("/deviceInfo/get/view/" + data.id);
        } else if (obj.event === 'rybgsq') {
            window.location.href = PageContext.getUrl("/rybgsq/toAdd?ryId=" + data.id);
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
            //Common.openFrame("/business/deviceInfo/add", "新增人员", '1200px', '1000px');
            window.location.href = PageContext.getUrl("/deviceInfo/toAdd");
        }
    };

    //打开新窗口
    function addTab(_this) {
        tab.tabAdd(_this);
    }
});

