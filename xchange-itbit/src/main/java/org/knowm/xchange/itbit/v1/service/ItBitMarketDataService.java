package org.knowm.xchange.itbit.v1.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.itbit.v1.ItBitAdapters;
import org.knowm.xchange.itbit.v1.dto.marketdata.ItBitDepth;
import org.knowm.xchange.itbit.v1.dto.marketdata.ItBitTicker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.util.List;

import static org.knowm.xchange.itbit.v1.ItBitAdapters.adaptCurrency;

public class ItBitMarketDataService extends ItBitMarketDataServiceRaw implements MarketDataService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public ItBitMarketDataService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
    CurrencyPair adaptedCurrencyPair = new CurrencyPair(adaptCurrency(currencyPair.base), adaptCurrency(currencyPair.counter));
    ItBitTicker itBitTicker = getItBitTicker(adaptedCurrencyPair);

    return ItBitAdapters.adaptTicker(adaptedCurrencyPair, itBitTicker);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    CurrencyPair adaptedCurrencyPair = new CurrencyPair(adaptCurrency(currencyPair.base), adaptCurrency(currencyPair.counter));
    ItBitDepth depth = getItBitDepth(adaptedCurrencyPair, args);

    List<LimitOrder> asks = ItBitAdapters.adaptOrders(depth.getAsks(), currencyPair, OrderType.ASK);
    List<LimitOrder> bids = ItBitAdapters.adaptOrders(depth.getBids(), currencyPair, OrderType.BID);

    return new OrderBook(null, asks, bids);
  }

  @Override
  public Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {
    CurrencyPair adaptedCurrencyPair = new CurrencyPair(adaptCurrency(currencyPair.base), adaptCurrency(currencyPair.counter));
    return ItBitAdapters.adaptTrades(getItBitTrades(adaptedCurrencyPair, args), currencyPair);
  }

}
