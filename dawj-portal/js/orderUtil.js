/**
 * Created by boil on 2016-4-15.
 */
App.Utils.OrderUtil = {
    getStatusText: function (status) {
        var text = '';
        switch (status) {
            case 'NEW':
                text = '待付款';
                break;
            case 'PAID':
                text = "已付款";
                break;
            case 'REFUNDABLE':
                text = "退款中";
                break;
            case 'COMPLETED':
                text = "已完成";
                break;
            case 'CLOSED':
                text = "已关闭";
                break;
        }
        return text;
    },
    getStatusTag: function (status) {
        var text = '';
        var css = '';
        switch (status) {
            case 'NEW':
                text = '待付款';
                var css = 'label-default';
                break;
            case 'PAID':
                text = "已付款";
                css = 'label-info';
                break;
            case 'REFUNDABLE':
                text = "退款中";
                css = 'label-warning';
                break;
            case 'COMPLETED':
                text = "已完成";
                css = 'label-success';
                break;
            case 'CLOSED':
                text = "已关闭";
                css = 'label-danger';
                break;
        }
        return '<span class="label ' + css + '">' + text + '</span>';
    }
}