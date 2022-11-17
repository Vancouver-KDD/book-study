# Data Clumps

Data Clumps, Data Hairball that lots of functions(cats) play with. But they are not in together.

As the book mentioned, it could be a sign of Feature Envy as well.

## 1. Extract Class

Book covered this part really well. Often, if data is broadly used in other places (you whiff Feature Envy here), that means the data also get along with related functions as well.

It makes the program leaner and cleaner by introducing a new class rather than create a single data object.

## 2. Introduce Parameter Object

But if the dataset truly doesn't need an extra fucntions, such as feature option set, function result struct, etc. they don't have to be a class. And depends on language features they might want to be a structure type due to `Pass by Value` or `Pass by Reference` problem

> Swift claims Structure and Class have very different needs. Structure is a value type which would be passed as **Value** and it will be always copied. Class, on the other hand, will be passed as reference and it won't be deep copied.

### Example

```c++
// Before
bool Registration::icp(const Geometry& source, const Geometry& target, 
    int icpAttempts, int icpIterations,
    double icpMovementTolerance, double rmseTolerance, ...);

// After
struct AlignParam
{
public:
    int samplingSize = 500;
    double overlappingTolerance = 0.1;
    int timeout = 5000;

    int icpAttempts = 50;
    int icpIterations = 30;
    double icpMovementTolerance = 0.01;
    double rmseTolerance = 0.05;

    int globalAttempts = 500;
    double globalRMSETolerance = 0.12;
    double corrMaxDistTol = std::numeric_limits<double>::max();
};

bool Registration::icp(const Geometry& source, const Geometry& target, AlignParam params = AlignParam())
{
    Eigen::Matrix4d prevMat;
    int failureCount = 0;
    for (int i = 0; i < params.icpAttempts; i++)
    {
        Eigen::Matrix4d mat = prevMat;
        if (!pointToPoint(source, target, mat))
        {
            failureCount++;
            continue;
        }

        double score = evaluation(source, target, mat);
        if (score < params.rmseTolerance)
            return true;
    }
}
```
