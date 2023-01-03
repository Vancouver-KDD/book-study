# Composite

### In One Liner

Define class structures in tree model to propagate responsibility of children management.

### Discussion Point

The book covers transparency VS safety aspect problem when implementing this tree-structured class hierarchy.

**Transparency** 
- Composites and Leafs contain same interfaces of Component.
- At leaf classes, it must check safety of relational operations since they do not support them.

**Safety**
- Leafs do not have **Relational** interfaces.
- At composite level, it should know whether the new class is leaf or composite.

[Why is transparency valued over safety in the Composite pattern?](https://stackoverflow.com/questions/36384165/why-is-transparency-valued-over-safety-in-the-composite-pattern)

The authors say **Transparency** over **Safety** is more valuable but they admitted that this concept is quite old and doesn't have to be considered as a problem since modern languages (even C++ as well) do have **interface** class and **Relational methods** only need to be conformed in Composite classes

[Refreshing Patterns, Rebecca J. Wirfs-Brock](https://www.wirfs-brock.com/PDFs/Refreshing%20Patterns.pdf)

### Pros 

- Solves lot of responsibility problems with recursive structure.
- Simplify Parent-Children composition by simple apis

### Cons

- Over-flexibility (over-generalize) may kill independency. Since defining generalized behavior requires quite a lot of work, accessing class-specific features or down-casted access will be very challenging.


### IRL Example

Polyga's private program uses Composite pattern to describe GUI widgets of [ImGui Library](https://github.com/ocornut/imgui).


UIBase(Component) -> Abstract class

UIContainer(Component Interface) -> Base class
    Window(Composite) -> Container Class
        ModalWindow(Composite)
        FloatingWindow(Composite)
        OptionWindow(Composite)
    Layout(Composite) -> Container Class
        VLayout(Composite)
        HLayout(Composite)

Text(Leaf)
Button(Leaf)
Checkbox(Leaf)
Combobox(Leaf)
ExpandButton(Leaf)
ExpandCheckbox(Leaf)
InputFloat(Leaf)
InputInt(Leaf)
Slider(Leaf)
...

Each window or layout can contain any type of UIBase and UIBase can be leaf or another window or layout.

UIBase is an abstract class itself and all the components implement UIBase. 