<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Chart</title>
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/style.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['line']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            const selectTicker = $('.hidden').val();
            const selectExchange = $('.select_exchange').text();

            $.ajax({
                url: '/coin',
                type: 'post',
                data: {
                    'ticker': selectTicker,
                    'exchange': selectExchange,
                },
                success: function (ticker) {
                    var data = new google.visualization.DataTable();
                    data.addColumn('datetime', 'Day');
                    data.addColumn('number', selectTicker);

                    var years = [];
                    var months = [];
                    var days = [];
                    var hours = [];
                    var minutes = [];

                    for (let i = 0; i < ticker.length; i++) {
                        let date = ticker[i].date.split('-');
                        let time = ticker[i].date.split(' ')[1].split(':');

                        years.push(parseInt(date[0]));
                        months.push(parseInt(date[1]));
                        days.push(parseInt(date[2]));
                        hours.push(time[0]);
                        minutes.push(time[1]);
                    }

                    var formatter = new google.visualization.NumberFormat({
                        fractionDigits: 4
                    });

                    let arr = new Array();
                    for (let i = 0; i < ticker.length; i++) {

                        let val = parseFloat(ticker[i].tradePrice);

                        arr[i] = [
                            new Date(years[i], months[i] - 1, days[i], hours[i], minutes[i]),
                            {v: val, f: val.toFixed(4)}
                        ]
                    }

                    data.addRows(arr)

                    formatter.format(data, 1);

                    var options = {
                        chart: {
                            title: 'CHART',
                            subtitle: `1USD : ${exchanges.get("KRW")} KRW, ${exchanges.get("JPY")} JPY`
                        },
                        width: 800,
                        height: 600,
                        hAxis: {
                            format: 'MM-dd HH:mm'
                        },
                        vAxis: {minValue: 0, format: 'decimal'},
                        explorer: {
                            actions: ['dragToZoom', 'rightClickToReset'],
                            axis: 'horizontal',
                            keepInBounds: true,
                            maxZoomIn: 4.0
                        }
                    };

                    var chart = new google.charts.Line(document.getElementById('linechart_material'));

                    chart.draw(data, google.charts.Line.convertOptions(options));
                }
            })
        }
    </script>
</head>
<body>
<div class="container">
    <input type="hidden" class="hidden" value="BTC"/>
    <div class="row">
        <div id="linechart_material" class="col"></div>
        <div class="col">
            <table class="thead">
                <colgroup>
                    <col width="33%">
                    <col width="33%">
                    <col width="33%">
                </colgroup>
                <thead>
                <th class="exchange select_exchange">USD</th>
                <th class="exchange">KRW</th>
                <th class="exchange">JPY</th>
                <script>
                    $('.exchange').on("click", function () {
                        $('.exchange').removeClass('select_exchange');
                        $(this).addClass('select_exchange');

                        drawChart();
                        drawList();
                    })
                </script>
                </thead>
            </table>
            <script type="text/javascript">

                drawList();

                function drawList() {
                    $.ajax({
                        url: '/coin/' + $('.select_exchange').text(),
                        type: 'post',
                        success: function (coins) {
                            let index;

                            if ($('.select_ticker').index() != -1) {
                                index = $('.select_ticker').index();
                            } else {
                                index = 0;
                            }

                            $('.list').html('');

                            for (let i = 0; i < coins.length; i++) {
                                $('.list').append(
                                    "<tr class='ticker'>" +
                                    "<td>" + coins[i].market + "</td>" +
                                    "<td>" + coins[i].tradePrice + "</td>" +
                                    "</tr>"
                                );
                            }

                            $('.list').children('tr').eq(index).addClass('select_ticker');

                            $('.ticker').on("click", function () {
                                $('.ticker').removeClass('select_ticker');
                                $(this).addClass('select_ticker');

                                const ticker = $(this).children('td:eq(0)').text();
                                $('.hidden').val(ticker);

                                drawChart();
                            });
                        }
                    });
                }
            </script>
            <div class="list_container">
                <table>
                    <colgroup>
                        <col width="40%"/>
                        <col width="60%"/>
                    </colgroup>
                    <tbody class="list">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
