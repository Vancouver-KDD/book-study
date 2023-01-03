# Template Method

### In One Liner

Use polymorphism on very-specific logics while leave shared common actions in the base class.

![](./images/template-method.png)
[reference](https://refactoring.guru/design-patterns/template-method)

### Point

Same as Strategy pattern, the goal of the pattern is to change behavior or algorithm proactively while reducing coupling and exposes of subclasses.

But the Template Method is a way of refactoring counter logics of shared codes while the Strategy pattern puts the whole logics into separate objects and be handled in runtime.

Since the Template Method forms the target logics into several subclasses, it is a static way of choosing the algorithm and it is done by polymorphism. On the other hand, Strategy uses composition (maybe with singleton as well) to keep multiple algorithms within runtime scope.

### Pros 

- Provides various algorithms by making only small changes
- Extends capability of logics to be more adaptive 

### Cons

- Over-generalizing might increase the maintanance cost
- Handling any critical edge case can be very painful

### IRL Example

There are tons of 3D Geometry file formats. They all have different specifications, encoding, supported data type, additional features, size, etc.

To handle them in a efficient way is to have subclasses for each format and let them handle format specific parsing and reading.

After that, Normal estimation, removing unconnected vertices, z depth colorization, recenter, or any other extra **post-processing** features can be done in a same manner thanks to the templated methods.

[ex. VCG Library File format](http://vcg.isti.cnr.it/vcglib/fileformat.html)

```c++
bool ReadMesh(const std::string& filename, Geometry* geometry)
{
    File* file = new File(filename);
    if (file == NULL) return false;

    std::string extension = getExtension(std::string(filename));

    if (stricompare(extension.c_str(), "stl"))
        return ReadSTL(file, geometry);
    else if (stricompare(extension.c_str(), "pbn"))
        return ReadPBN(file, geometry);
    else if (stricompare(extension.c_str(), "obj"))
        return ReadOBJ(file, geometry);
    else if (stricompare(extension.c_str(), "ply"))
        return ReadPLY(file, geometry);
    else if (stricompare(extension.c_str(), "asc"))
        return ReadASC(file, geometry);
    else if (stricompare(extension.c_str(), "csv"))
        return ReadCSV(file, geometry);
    else if (stricompare(extension.c_str(), "pts"))
        return ReadPTS(file, geometry);
    else if (stricompare(extension.c_str(), "e57"))
        return ReadE57(file, geometry);

    return false;
}

Geometry* ImportFile(const std::string& file)
{
    Geometry* newG = new Geometry();
    ReadMesh(file, newG);
    
    if (SettingManager::I().GetBoolean(Import_Decimation))
    {
        int percentage = SettingManager::I().GetInt(Import_Decimation_Percentage);
        if (percentage == 0) percentage = 1;
        Decimate(newG, percentage);
    }

    // In Correct Normals
    if (newG->nNormals() != newG->nVertices())
    {
        newG->mNormals.clear();
    }

    // Normal Generation 
    if (newG->nNormals() == 0) 
    {        
        EstimateNormals(newG);
    }
    else
    {
        NormalizeNormals(newG);
    }

    // scaling
    if (SettingManager::I().GetBoolean(Import_Scale))
    {
        float scaleUnit = SettingManager::I().GetInt(Import_Scale_Factor);
        Scale(newG, scaleUnit);
    }

    if (SettingManager::I().GetBoolean(Import_Recenter))
    {        
        CenterGeometry(newG);
    }

    if (SettingManager::I().GetBoolean(Import_Remove_Duplicates))
    {
        RemoveDuplicates(newG);
    }

    return newG;
}
```