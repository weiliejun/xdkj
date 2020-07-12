layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols = [[
        /*{
            checkbox: true,
            width: 60,
            fixed: true
        },*/{
            field: 'ordId',
            width: 220,
            title: '流水号',
            align: 'center'
        }, {
            field: 'userName',
            width: 220,
            title: '用户名',
            align: 'center',
            sort: true
        }, {
            field: 'userMobile',
            width: 220,
            title: '手机号码',
            align: 'center'
        }, {
            field: 'topupAmount',
            width: 220,
            title: '充值金额（元）',
            align: 'center'
        }, {
            field: 'status',
            width: 150,
            title: '充值状态',
            align: 'center',
            templet: '#status'
        }, {
            field: 'topupType',
            width: 150,
            title: '支付方式',
            align: 'center',
            templet: '#topupType'
        }, {
            field: 'createTime',
            width: 220,
            title: '充值时间',
            align: 'center',
            templet: '#createTime'
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#userTopUpBar'
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#userTopUpTables', '/userTopUp/list', cols, table);

    //监听工具条
    table.on('tool(userTopUpTables)', function (obj) {
        var data = obj.data;
        // 设为成功
        if (obj.event === 'editStatus') {
            var reqData = {userTopupId: data.id};
            Common.confirmFrame('/userTopUp/editStatus', '确定要修改充值状态么？', reqData, 'userTopUpTables', table);
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