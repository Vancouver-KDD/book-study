/**
 * Proxy is a structural design pattern
 * that provides an object that acts as a substitute for a real service object used by a client.
 * A proxy receives client requests, does some work (access control, caching, etc.)
 * and then passes the request to a service object.
 */

interface Subject {
  request(): void;
}

class RealSubject implements Subject {
  public request(): void {
      console.log('RealSubject: Handling request.');
  }
}

class Proxy implements Subject {
  private realSubject: RealSubject;

  constructor(realSubject: RealSubject) {
      this.realSubject = realSubject;
  }

  public request(): void {
      if (this.checkAccess()) {
          this.realSubject.request();
          this.logAccess();
      }
  }

  private checkAccess(): boolean {
      console.log('Proxy: Checking access prior to firing a real request.');

      return true;
  }

  private logAccess(): void {
      console.log('Proxy: Logging the time of request.');
  }
}

function clientCode(subject: Subject) {
  subject.request();
}

console.log('Client: Executing the client code with a real subject:');
const realSubject = new RealSubject();
clientCode(realSubject);

console.log('');

console.log('Client: Executing the same client code with a proxy:');
const proxy = new Proxy(realSubject);
clientCode(proxy);
