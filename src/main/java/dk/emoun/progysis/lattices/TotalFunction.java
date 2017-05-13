package dk.emoun.progysis.lattices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Represents a Total Function of the key elements K, and lattice elements V.<br>
 * Each instance of a {@link TotalFunction} tracks a set of (changeble) keys.
 * If a key used that is not tracked, a default lattice element is used as the mapping for that key.
 *  * 
 * @param <R>
 * The type of the {@link TotalFunction Total Function} extender(IE R should be the exact same type as the extending class)
 * @param <K>
 * The key type of the Total Function. 
 * I.E. The K in the Total Function definition: (K->V, <=)
 * @param <V>
 * The lattice element type the total function maps to.
 * I.E. The V in the Total Function definition: (K->V, <=)
 */
public abstract class TotalFunction
	<
		R extends TotalFunction<R,K,V>,
		K,
		V extends LatticeElement<V>
	> 
	extends LatticeElement<R> 
{
	
//Fields	
	/**
	 * Mapping of keys to lattice elements.<br>
	 * Effectively the state of the Total Function.<br>
	 * Given a Total Function 'f', 'f(s)' == mapping.get(s).
	 */
	private Map<K,V> mapping;
	
	/**
	 * The default element for the total function.
	 */
	private V lattice;
//Constructors
	/**
	 * Constructs a Total Function over the given lattice, mapping the given keys.
	 * @param lattice
	 * The lattice element the resulting TotalFunction defaults to.
	 * @param keys
	 * Specific key values the TotalFunction is to map. <br>
	 * Duplicate keys are only mapped once by the resulting TotalFunction.<br>
	 * All keys evaluate to the given element when the constructor returns.<br>
	 * 
	 */
	public TotalFunction(V lattice, K... keys){
		this(new HashMap<K,V>(), lattice);
		for(K key: keys){
			this.mapping.put(key, lattice);
		}
	}
	
	/**
	 * Constructs a Total Function with the given mapping and default element.
	 * @param mapping
	 * Mapping from a key set to elements of the given lattice.
	 * @param lattice
	 * The default element of the total function.
	 */
	protected TotalFunction(Map<K,V> mapping, V lattice){
		this.mapping = mapping;
		this.lattice = lattice;
	}
	
//Abstract methods
	/**
	 * Constructs a new Total Function with the given mapping to the same lattice elements as
	 * the instance this method is invoked on.<br>
	 * <br>
	 * Simply, an implementation could be:<br>
	 * protected R constructTotalFunction(Map<K,V> mapping){<br>
	 * return new R(mapping, V);<br>
	 * }<br>
	 * 
	 * @param mapping
	 * The mapping of a key set to elements of the same lattice 
	 * as the instance this method is invoked on.
	 * 
	 * @param defaultElement
	 * Default lattice element of the resulting total function.
	 * @return
	 * The newly constructed instance.
	 */
	protected abstract R constructTotalFunction(Map<K,V> mapping, V defaultElement);
	
	
//Methods

	/**
	 * Evaluates the Total Function mapping of the given key.<br>
	 * @param key
	 * The key mapping to evaluate.
	 * @return
	 * The element the given key maps to in the TotalFunction this method is invoked on.<br>
	 * If the TotalFunction does not map that key, the default lattice element is returned.
	 */
	public V getValue(K key){
		V value = mapping.get(key);
		return (value ==  null)? lattice : value;
	}
	
	/**
	 * Returns a new Total Function instance where the given key mapping holds.<br>
	 * If the instance does not already map the given key,
	 * the resulting instance will map the given key in addition to
	 * all the keys the invoking instance maps.<br>
	 * The default element of the resulting total function is identical to 
	 * the invoked instance's default.
	 * @param key
	 * The key value to map
	 * @param e
	 * lattice element to map to.
	 * @return
	 * a new Total Function that is identical to the invoking instance except that it
	 * maps the given key to the given lattice element.<br>
	 */
	public R getUpdateValue(K key, Evaluable<V> e){
		HashMap<K,V> newMapping = new HashMap<K,V>();
		newMapping.putAll(mapping);
		newMapping.put(key, e.value());
		return constructTotalFunction(newMapping, lattice);
	}
	
	/**
	 * Constructs a new Total Function instance that maps the given keys to
	 * the bottom value of the lattice.<br>
	 * The default lattice element of the resulting total function is also bottom.
	 * @param keys
	 * Key set the resulting instance maps. Duplicates are only mapped once.
	 * @return
	 * A new Total function that maps the given keys to bottom
	 */
	public R constructBottomTotalFunction(K... keys){
		Map<K, V> mapping = new HashMap<K, V>();
		for(K key: keys){
			mapping.put(key, lattice.getBottom());
		}
		return constructTotalFunction(mapping, lattice.getBottom());
	}
	
//Accessors
	
	/**
	 * @return
	 * The default lattice element of the instance.
	 */
	public V getDefault(){
		return this.lattice;
	}
	
//Overriding methods
		
	@Override
	public R getBottom() {
		return copyThisToFunctionWhereAllKeysMapTo(lattice.getBottom());
	}
	
	@Override
	public boolean isBottom() {
		return allValuesMapTo(lattice.getBottom());
	}

	@Override
	public boolean compare(Evaluable<R> other){
		
		TotalFunction<R,K,V> o = (TotalFunction) (other.value());
		
		if( !mapsKeysOf(this, o) || 
			!mapsKeysOf(o, this))
		{
			return false;
		}
		
		if(isBottom())
		{
			return true;
		}
		
		if(other.value().isBottom())
		{
			return false;
		}
		
		//At this point neither is bottom and they map the same keys
		for(K key: mapping.keySet()){
			if(!this.getValue(key).compare(
					o.getValue(key)
					))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public R join(Evaluable<R> other) {
		TotalFunction<R,K,V> e2TF = (TotalFunction) (other.value());
		
		HashMap<K,V> newMapping = new HashMap<K,V>();
		
		//Get all keys from both functions
		Set<K> allKeys = joinKeySets(this, e2TF);
		
		//Join the two values for each key and put it in the new map
		V v1, v2;
		for(K key: allKeys){
			v1 = getValueOrBottom(this, key);
			v2 = getValueOrBottom(e2TF, key);
			
			newMapping.put(key, v1.join(v2));
		}
		
		return constructTotalFunction(newMapping, lattice.join(e2TF.lattice));
	}

	@Override
	public String stringRepresentation(){
		if(mapping.isEmpty()){
			return "{}";
		}
		StringBuilder b = new StringBuilder();
		
		b.append('{');
		for(Entry<K,V> e: mapping.entrySet()){
			b.append('{');
			b.append(e.getKey().toString());
			b.append('=');
			b.append(e.getValue().stringRepresentation());
			b.append('}');
		}
		b.append('}');
		return b.toString();		
	}
		
//private methods
	
	/**
	 * Get the lattice element that the given Total Function maps the given to.<br>
	 * If the given function does not map the key, the bottom element of the lattice
	 * is returned instead.
	 * @param e
	 * @param key
	 * @return
	 */
	private V getValueOrBottom(TotalFunction<R, K, V> e, K key) {
		try{
			return e.getValue(key);
		}catch(UnmappedKeyException err){
			return lattice.getBottom();
		}
	}
	
	/**
	 * Returns a new set of keys, which contains all the keys both given Total Functions
	 * map.
	 * @param e1T
	 * @param e2T
	 * @return
	 */
	private Set<K> joinKeySets(TotalFunction<R, K, V> e1T, TotalFunction<R, K, V> e2T) {
		Set<K> allKeys = new HashSet<K>();
		allKeys.addAll(e1T.mapping.keySet());
		allKeys.addAll(e2T.mapping.keySet());
		return allKeys;
	}
		
	/**
	 * Returns whether the first Total Function maps
	 * all of the second Total Function's keys.
	 * @param maps
	 * @param keys
	 * @return
	 * whether {@code maps} maps all of the keys {@code keys'} maps. 
	 */
	private boolean mapsKeysOf(TotalFunction<R, K, V> maps, TotalFunction<R, K, V> keys){
		for(K key: keys.mapping.keySet()){
			if(!maps.mapping.containsKey(key)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Copies the given maps' keys into a new map, and maps
	 * then all to the given value.
	 * @param value
	 * @return
	 * The resulting map
	 */
	private Map<K,V> copyKeysWithValue(Map<K,V> keys, V value) {
		Map<K,V> newKeyMapping = new HashMap<K,V>();
		for(K key: keys.keySet()){
			newKeyMapping.put(key, value);
		}
		
		return newKeyMapping;
	}
	
	/**
	 * Creates a new Total Function that maps the same keys
	 * as the invoking instance, but where all keys map to
	 * the given value. The default of the resulting instance
	 * is the same as the invoking instance.
	 * @param e
	 * Complete Lattice element to map all the keys to.
	 * @return
	 * The resulting Total Function instance
	 */
	private R copyThisToFunctionWhereAllKeysMapTo(V e) {
		return constructTotalFunction(copyKeysWithValue(mapping, e), lattice);
	}
	
	/**
	 * Returns whether the given Total Function maps
	 * all of its keys to the given value.
	 * @param r
	 * @param e
	 * @return
	 */
	private boolean allValuesMapTo(Evaluable<V> e){
		
		for(Entry<K,V> mapping: mapping.entrySet()){
			if(!LatticeUtilities.equal(mapping.getValue(), e)){
				return false;
			}
		}
		return true;
	}
}
