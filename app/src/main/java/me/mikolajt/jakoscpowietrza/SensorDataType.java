package me.mikolajt.jakoscpowietrza;

public enum SensorDataType {

    CO2("ppm", 900), TEMPERATURE("°C", 25), AIR_PRESSURE("hPa", 1020), HUMIDITY("%", 60);

    final String unit;
    final int upperLimit;

    SensorDataType(String unit, int upperLimit){
        this.unit = unit;
        this.upperLimit = upperLimit;
    }
}
