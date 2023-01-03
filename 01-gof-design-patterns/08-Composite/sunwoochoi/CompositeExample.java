/*
  test output:
  Render container root
  Render container header
  Render single component header-Home
  Render single component header-Blog
  Render single component header-Contact
  Render container body
  Render single component body-hello world!
  Render single component footer-MIT license

  Render container root
  Render container body
  Render single component body-hello world!
  Render single component footer-MIT license
 */
class CompositeExample {
  /* Client */
  public static void main(String[] args) {
    ParentComponent rootComponent = new ParentComponent("root");

    ParentComponent headerComponent = new ParentComponent("header");
    ChildComponent headerChild1 = new ChildComponent("header-Home");
    ChildComponent headerChild2 = new ChildComponent("header-Blog");
    ChildComponent headerChild3 = new ChildComponent("header-Contact");

    headerComponent.addComponent(headerChild1);
    headerComponent.addComponent(headerChild2);
    headerComponent.addComponent(headerChild3);

    ParentComponent bodyComponent = new ParentComponent("body");
    ChildComponent bodyChild1 = new ChildComponent("body-hello world!");
    bodyComponent.addComponent(bodyChild1);

    Component footerComponent = new ChildComponent("footer-MIT license");

    rootComponent.addComponent(headerComponent);
    rootComponent.addComponent(bodyComponent);
    rootComponent.addComponent(footerComponent);

    rootComponent.render();

    rootComponent.removeComponent(headerComponent);
    rootComponent.render();
  }
}
