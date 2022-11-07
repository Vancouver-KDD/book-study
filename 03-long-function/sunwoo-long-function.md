# Long Function

## Replace Temp with Query
```ts
class Humidifier {
    private temperature: number
    private humidity: number

    constructor(temperature: number, humidity: number) {
        this.temperature = temperature
        this.humidity = humidity
    }

    checkStatus(): string {
        // this computation is repeated in other functions in class
        const status = this.temperature * this.humidity 
        if (status < 20) {
            return "Less Humidity"
        } else if (status < 30) {
            return "Normal"
        } else {
            return "More Humidity"
        }
    }

    autoManage(): void {
        if (this.temperature * this.humidity > 30) {
            console.log("Turn off humidifier")
        } else {
            console.log("Turn on humidifier")
        }
    }
}
```

```ts
class RefactoredHumidifier {
    private temperature: number
    private humidity: number

    constructor(temperature: number, humidity: number) {
        this.temperature = temperature
        this.humidity = humidity
    }

    // extract computation to a getting method
    get status() { return  this.temperature * this.humidity }

    checkStatus(): string {
        if (this.status < 20) {
            return "Less Humidity"
        } else if (this.status < 30) {
            return "Normal"
        } else {
            return "More Humidity"
        }
    }

    autoManage(): void {
        if (this.status > 30) {
            console.log("Turn off humidifier")
        } else {
            console.log("Turn on humidifier")
        }
    }
}
```

