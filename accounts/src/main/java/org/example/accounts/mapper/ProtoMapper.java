package org.example.accounts.mapper;

import com.example.grpc.CurrencyProto;
import com.example.grpc.DecimalValue;
import com.google.protobuf.ByteString;
import io.swagger.client.model.Currency;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.exception.InvalidCurrencyParamException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@Slf4j
@Component
public class ProtoMapper {
    public BigDecimal mapDecimalValueToBigDecimal(DecimalValue proto) {
        MathContext mc = new MathContext(proto.getPrecision());
        return new BigDecimal(
                new BigInteger(proto.getValue().toByteArray()),
                proto.getScale(),
                mc);
    }

    public Currency mapCurrencyProtoToCurrency(CurrencyProto proto) {
        log.info("mapping " + proto.name());
        Currency result = Currency.fromValue(proto.name());
        if (result == null) throw new InvalidCurrencyParamException(proto.name());
        return result;
    }

    public CurrencyProto mapCurrencyToCurrencyProto(Currency c) {
        log.info("mapping " + c.name());
        CurrencyProto result = CurrencyProto.valueOf(c.name());
        if (result.equals(CurrencyProto.UNRECOGNIZED)) throw new InvalidCurrencyParamException(c.name());
        return result;
    }

    public DecimalValue mapBigDecimalToDecimalValue(BigDecimal value) {
        return DecimalValue.newBuilder()
                .setScale(value.scale())
                .setPrecision(value.precision())
                .setValue(ByteString.copyFrom(value.unscaledValue().toByteArray()))
                .build();
    }
}
