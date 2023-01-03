# Insider Trading

The article below explains the problem very well.

Let's say there is two modules, A and B. B references and uses A's features.

But due to visibility of A's features, B actually knows how they are implemented. Thus, B pretends it knows everything about A and regarding codes are written based on that *Intel*.

But later on, A, who doesn't know about any dependents, wants to change its internal implementations and also APIs. That breaks lots of B's code because it **pretended** Mr. knows everything.

[What is Insider Trading bad smell](https://softwareengineering.stackexchange.com/questions/421437/what-is-insider-trading-bad-smell)

## Move Functions & Fields

These hidden access should not hide under the surface. Bring them out to expose their connections.

## Hide Delegate

If there are multiple bonds, make a intermediary object as a delegate to regulate common interests in between.

## Replace Subclass & Superclass with Delegate

If the subclass works as a form of delegation later on, it could be a good option to switch it to a composited delegation object.

And especially, even though inheritance is innocent, certain cases encourage

The *Before* example implements Booking and PremiumBooking. Some of Booking functions only makes sense due to the PremiumBooking.

```ts
//Before
class Booking {
    private isPeakDay: bool;

    constructor(show, date) {
        this._show = show;
        this._date = date;
    }

    get hasTalkback(): bool {
        return this._show.hasOwnProperty('talkback') && !this.isPeakDay;
    }

    get basePrice(): number {
        let result = this._show.price;
        if (this.isPeakDay)
            result += Math.round(result * 0.15)
        return result;
    }
}

class PremiumBooking extends Booking {
    constructor(show, date, extras) {
        super(show, date);
        this._extras = extras;
    }

    get hasTalkback(): bool {
        return this._show.hasOwnProperty('talkback');
    }

    get basePrice(): number {
        return Math.round(super.basePrice + this._extras.premiumFree);
    }

    get hasDinner(): bool {
        return this._extras.hasOwnProperty('dinner') && !this.isPeakDay;
    }
}

//After
function createBooking(show: Show, date: string) {
    return new Booking(show, date);
}
function createPremiumBooking(show: Show, date: string, extras: Extra) {
    const newBooking = new Booking(show, date);
    newBooking._bePremium(extras);
    return newBooking;
}

class Booking {
    private isPeakDay: bool;
    private _show: Show;
    private _date: string;
    private _premiumDelegate: PremiumBookingDelegate;

    constructor(show: Show, date: string) {
        this._show = show;
        this._date = date;
    }

    function _bePremium(extras: Extra) {
        this._premiumDelegate = new PremiumBookingDelegate(this, extras);
    }

    get hasTalkback(): bool {
        return (this._premiumDelegate)
            ? this._premiumDelegate.hasTalkback
            : this._show.hasOwnProperty('talkback') && !this.isPeakDay;
    }

    get basePrice(): number {
        let result = this._show.price;
        if (this.isPeakDay) result += Math.round(result * 0.15);
        return (this._premiumDelegate)
            ? this._premiumDelegate.extendBasePrice(result) 
            : result;
    }

    get hasDinner(): bool {
        return (this._premiumDelegate)
            ? this._premiumDelegate.hasDinner
            : undefined;
    }
}

class PremiumBookingDelegate {
    private _host: Booking;
    private _extras: Extra; 

    constructor(booking: Booking, extras: Extra) {
        this._host = booking;
        this._extras = extras;
    }

    get hasTalkback(): bool {
        return this._host._show.hasOwnProperty('talkback');
    }

    get basePrice(): number {
        return Math.round(this._host._privateBasePrice + this._extras.premiumFee);
    }

    function extendBasePrice(base: number): number {
        return Math.round(base + this._extras.premiumFee);
    }

    get hasDinner(): bool {
        return this._extras.hasOwnProperty('dinner') && !this._host.isPeakDay;
    }
}
```
