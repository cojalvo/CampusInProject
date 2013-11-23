package il.ac.shenkar.common;

//TODO Not in use  remove in the end.
public class KeyValue<K, V>
{
    private K key;
    private V value;

    public K getKey()
    {
	return key;
    }

    public void setKey(K key)
    {
	this.key = key;
    }

    public V getValue()
    {
	return value;
    }

    public void setValue(V value)
    {
	this.value = value;
    }

}
