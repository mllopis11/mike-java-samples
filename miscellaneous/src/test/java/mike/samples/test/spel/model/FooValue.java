package mike.samples.test.spel.model;

public class FooValue {

    private final FooValueType type;
    private final String name;
    private final String rawValue;
    private final Object objValue;

    protected FooValue(FooValueType type, String name, String rawValue, Object objValue) {
	this.type = type;
	this.name = name;
	this.rawValue = rawValue;
	this.objValue = objValue;
    }

    public FooValueType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getRawValue() {
        return rawValue;
    }

    public Object getObjValue() {
        return objValue;
    }

    @Override
    public String toString() {
	return String.format("FooValue [type=%s, name=%s, rawValue=%s, objValue=%s]", type, name, rawValue, objValue);
    }
}
