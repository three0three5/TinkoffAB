package org.example.app.mapper;

import com.example.grpc.CurrencyProto;
import com.example.grpc.DecimalValue;
import com.google.protobuf.ByteString;
import io.swagger.client.model.Currency;
import lombok.extern.slf4j.Slf4j;
import org.example.app.exception.CurrencyNotAvailableException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@Slf4j
@Component
public class ProtoMapper {
    public BigDecimal mapDecimalValueToBigDecimal(DecimalValue proto) {
        MathContext mc = new java.math.MathContext(proto.getPrecision());
        return new BigDecimal(
                new BigInteger(proto.getValue().toByteArray()),
                proto.getScale(),
                mc);
    }

    public Currency mapCurrencyProtoToCurrency(CurrencyProto proto) {
        log.info("mapping " + proto.name());
        Currency result = Currency.fromValue(proto.name());
        if (result == null) throw new CurrencyNotAvailableException(proto.name());
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
