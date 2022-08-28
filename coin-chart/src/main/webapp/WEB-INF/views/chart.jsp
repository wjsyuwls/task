<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Chart</title>
    <link href="resources/css/index.css" rel="stylesheet">
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <script src="webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['line']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            const selectTicker = $('.hidden').val();

            $.ajax({
                url: 'chart/coin',
                type: 'post',
                data: {'ticker': selectTicker},
                success: function (ticker) {
                    var data = new google.visualization.DataTable();
                    data.addColumn('datetime', 'Day');
                    data.addColumn('number', selectTicker);

                    var years = [];
                    var months = [];
                    var days = [];
                    var hours = [];
                    var minutes = [];

                    for (var i = 0; i < ticker.length; i++) {
                        let date = ticker[i].date.split('-');
                        let time = ticker[i].date.split(' ')[1].split(':');

                        years.push(parseInt(date[0]));
                        months.push(parseInt(date[1]));
                        days.push(parseInt(date[2]));
                        hours.push(time[0]);
                        minutes.push(time[1]);
                    }

                    let arr = new Array();
                    for (var i = 0; i < ticker.length; i++) {
                        arr[i] = [new Date(years[i], months[i] - 1, days[i], hours[i], minutes[i]), Math.floor(ticker[i].tradePrice)]
                    }

                    data.addRows(arr)

                    var options = {
                        chart: {
                            title: 'CHART',
                            subtitle: `1USD : ${exchange.get("KRW")} KRW, ${exchange.get("JPY")} JPY`
                        },
                        width: 800,
                        height: 600,
                        hAxis: {
                            format: 'MM-dd HH:mm'
                        },
                        vAxis: {minValue: 0, format: 'decimal'},
                    };

                    var chart = new google.charts.Line(document.getElementById('linechart_material'));

                    chart.draw(data, google.charts.Line.convertOptions(options));
                }
            })
        }
    </script>
</head>
<body>
<div class="container" style="margin-top: 100px">
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
                        $('.ticker').removeClass('select_ticker');

                        $('.exchange').removeClass('select_exchange');
                        $(this).addClass('select_exchange');

                        const exchange = $(this).text();

                        if (exchange == 'USD') {
                            $('.usd:eq(0)').addClass('select_ticker');

                            $('.exchange_text').text('/USD');
                            $('.ticker').addClass('hide');
                            $('.usd').removeClass('hide');
                        } else if (exchange == 'KRW') {
                            $('.krw:eq(0)').addClass('select_ticker');

                            $('.exchange_text').text('/KRW');
                            $('.ticker').addClass('hide');
                            $('.krw').removeClass('hide');
                        } else if (exchange == 'JPY') {
                            $('.jpy:eq(0)').addClass('select_ticker');

                            $('.exchange_text').text('/JPY');
                            $('.ticker').addClass('hide');
                            $('.jpy').removeClass('hide');
                        }
                    })
                </script>
                </thead>
            </table>
            <div class="tbody">
                <table>
                    <colgroup>
                        <col width="10%">
                        <col width="40%">
                        <col width="50%">
                    </colgroup>
                    <tbody class="list">
                    <c:forEach var="coin" items="${coins}">
                        <c:if test="${coin.currencyCode.equals('USD')}">
                            <tr class="ticker usd">
                                <td></td>
                                <td colspan="1"><span>${coin.market}</span><span class="exchange_text">/USD</span></td>
                                <td colspan="1" class="trade_price">${coin.tradePrice}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    <c:forEach var="coin" items="${coins}">
                        <c:if test="${coin.currencyCode.equals('KRW')}">
                            <tr class="ticker krw hide">
                                <td></td>
                                <td colspan="1"><span>${coin.market}</span><span class="exchange_text">/USD</span></td>
                                <td colspan="1" class="trade_price">${coin.tradePrice}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    <c:forEach var="coin" items="${coins}">
                        <c:if test="${coin.currencyCode.equals('JPY')}">
                            <tr class="ticker jpy hide">
                                <td></td>
                                <td colspan="1"><span>${coin.market}</span><span class="exchange_text">/JPY</span></td>
                                <td colspan="1" class="trade_price">${coin.tradePrice}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                    <script type="text/javascript">
                        //default
                        $('.list tr:eq(0)').addClass('select_ticker');

                        $('.ticker').on("click", function () {
                            $('.ticker').removeClass('select_ticker');
                            $(this).addClass('select_ticker');

                            const ticker = $(this).children('td:eq(1)').text().split('/')[0];
                            $('.hidden').val(ticker);

                            drawChart();
                        });
                    </script>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
