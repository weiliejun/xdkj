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
            field: 'dlsmc',
            title: '代理商名称',
            align: 'center',
            width: '15%',
            sort: true
        }, {
            field: 'dlsjc',
            title: '代理商简称',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'lxr',
            title: '联系人',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'lxdh',
            title: '联系电话',
            align: 'center',
            width: '10%',
            sort: true
        }, {
            field: 'txdz',
            title: '通讯地址',
            align: 'center',
            width: '20%',
            sort: true
        }, {
            field: 'dlzs',
            title: '代理证书',
            align: 'center',
            width: '10%',
            sort: true
        }/*, {
            field: 'createTime',
            title: '创建时间',
            align: 'center',
            width: '10%',
            sort: true
        }*/, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#dlsInfoBar',
            width: '20%',
            sort: true
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#dlsInfoTables', '/dlsInfo/list', cols, table);

    //监听工具条
    table.on('tool(dlsInfoTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            window.location.href = PageContext.getUrl("/dlsInfo/get/update/" + data.id);
        } else if (obj.event === 'view') {
            window.location.href = PageContext.getUrl("/dlsInfo/get/view/" + data.id);
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
            //Common.openFrame("/business/dlsInfo/add", "新增人员", '1200px', '1000px');
            window.location.href = "/dlsInfo/toAdd";
        }
    };

    //打开新窗口
    function addTab(_this) {
        tab.tabAdd(_this);
    }
});

