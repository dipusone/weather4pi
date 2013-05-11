package weather4pi.lcd;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.RaspiPin;

public class LcdFactory {
	
	private static final LCD weatherLcd = new GpioLcdDisplay(
											2,          // number of row supported by LCD
						                    16,       // number of columns supported by LCD
						                    RaspiPin.GPIO_11,  // LCD RS pin
						                    RaspiPin.GPIO_10,  // LCD strobe pin
						                    RaspiPin.GPIO_00,  // LCD data bit 1
						                    RaspiPin.GPIO_01,  // LCD data bit 2
						                    RaspiPin.GPIO_02,  // LCD data bit 3
						                    RaspiPin.GPIO_03); // LCD data bit 4
	
	public static LCD createWeather4PiLcd(){
		return weatherLcd;
	}
}
