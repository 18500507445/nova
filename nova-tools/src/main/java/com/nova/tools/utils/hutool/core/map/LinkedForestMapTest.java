package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.ForestMap;
import cn.hutool.core.map.LinkedForestMap;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.TreeEntry;
import cn.hutool.core.lang.Assert;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import java.util.*;

public class LinkedForestMapTest {

	private final ForestMap<String, String> treeNodeMap = new LinkedForestMap<>(false);

	@Before("")
	public void beforeTest() {
		// a -> b -> c -> d
		treeNodeMap.putLinkedNodes("a", "b", "bbb");
		treeNodeMap.putLinkedNodes("b", "c", "ccc");
		treeNodeMap.putLinkedNodes("c", "d", "ddd");
		treeNodeMap.get("a").setValue("aaa");
	}

	@Test
	public void testTreeEntry() {
		final TreeEntry<String, String> parent = treeNodeMap.get("b");
		final TreeEntry<String, String> treeEntry = treeNodeMap.get("c");
		final TreeEntry<String, String> child = treeNodeMap.get("d");

		// Entry相关
		Assert.equals("c", treeEntry.getKey());
		Assert.equals("ccc", treeEntry.getValue());

		// 父节点相关方法
		Assert.equals(2, treeEntry.getWeight());
		Assert.equals(treeNodeMap.get("a"), treeEntry.getRoot());
		Assert.isTrue(treeEntry.hasParent());
		Assert.equals(parent, treeEntry.getDeclaredParent());
		Assert.equals(treeNodeMap.get("a"), treeEntry.getParent("a"));
		Assert.isTrue(treeEntry.containsParent("a"));

		// 子节点相关方法
		final List<TreeEntry<String, String>> nodes = new ArrayList<>();
		treeEntry.forEachChild(true, nodes::add);
		Assert.equals(CollUtil.newArrayList(treeEntry, child), nodes);
		nodes.clear();
		treeEntry.forEachChild(false, nodes::add);
		Assert.equals(CollUtil.newArrayList(child), nodes);

		Assert.equals(CollUtil.newLinkedHashSet(child), new LinkedHashSet<>(treeEntry.getDeclaredChildren().values()));
		Assert.equals(CollUtil.newLinkedHashSet(child), new LinkedHashSet<>(treeEntry.getChildren().values()));
		Assert.isTrue(treeEntry.hasChildren());
		Assert.equals(treeNodeMap.get("d"), treeEntry.getChild("d"));
		Assert.isTrue(treeEntry.containsChild("d"));
	}

	@Test
	public void putTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		TreeEntry<String, String> treeEntry = new LinkedForestMap.TreeEntryNode<>(null, "a", "aaa");
		Assert.isNull(map.put("a", treeEntry));
		Assert.notEquals(map.get("a"), treeEntry);
		Assert.equals(map.get("a").getKey(), treeEntry.getKey());
		Assert.equals(map.get("a").getValue(), treeEntry.getValue());

		treeEntry = new LinkedForestMap.TreeEntryNode<>(null, "a", "aaaa");
		Assert.notNull(map.put("a", treeEntry));
		Assert.notEquals(map.get("a"), treeEntry);
		Assert.equals(map.get("a").getKey(), treeEntry.getKey());
		Assert.equals(map.get("a").getValue(), treeEntry.getValue());
	}

	@Test
	public void removeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		map.remove("b");
		Assert.isNull(map.get("b"));
		Assert.isFalse(b.hasChildren());
		Assert.isFalse(b.hasParent());
		Assert.equals(a, c.getDeclaredParent());
		Assert.equals(CollUtil.newArrayList(c), new ArrayList<>(a.getDeclaredChildren().values()));
	}

	@Test
	public void putAllTest() {
		final ForestMap<String, String> source = new LinkedForestMap<>(false);
		source.linkNodes("a", "b");
		source.linkNodes("b", "c");

		final ForestMap<String, String> target = new LinkedForestMap<>(false);
		target.putAll(source);

		final TreeEntry<String, String> a = target.get("a");
		final TreeEntry<String, String> b = target.get("b");
		final TreeEntry<String, String> c = target.get("c");

		Assert.notNull(a);
		Assert.equals("a", a.getKey());
		Assert.equals(CollUtil.newArrayList(b, c), new ArrayList<>(a.getChildren().values()));

		Assert.notNull(b);
		Assert.equals("b", b.getKey());
		Assert.equals(CollUtil.newArrayList(c), new ArrayList<>(b.getChildren().values()));

		Assert.notNull(c);
		Assert.equals("c", c.getKey());
		Assert.equals(CollUtil.newArrayList(), new ArrayList<>(c.getChildren().values()));

	}

	@Test
	public void clearTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");
		Assert.isFalse(a.hasParent());
		Assert.isTrue(a.hasChildren());
		Assert.isTrue(b.hasParent());
		Assert.isTrue(b.hasChildren());
		Assert.isTrue(c.hasParent());
		Assert.isFalse(c.hasChildren());

		map.clear();
		Assert.isTrue(map.isEmpty());
		Assert.isFalse(a.hasParent());
		Assert.isFalse(a.hasChildren());
		Assert.isFalse(b.hasParent());
		Assert.isFalse(b.hasChildren());
		Assert.isFalse(c.hasParent());
		Assert.isFalse(c.hasChildren());
	}

	@Test
	public void getNodeValueTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.putNode("a", "aaa");
		Assert.equals("aaa", map.getNodeValue("a"));
		Assert.isNull(map.getNodeValue("b"));
	}

	@Test
	public void putAllNodeTest() {
		final ForestMap<String, Map<String, String>> map = new LinkedForestMap<>(false);

		final Map<String, String> aMap = MapBuilder.<String, String> create()
			.put("pid", null)
			.put("id", "a")
			.build();
		final Map<String, String> bMap = MapBuilder.<String, String> create()
			.put("pid", "a")
			.put("id", "b")
			.build();
		final Map<String, String> cMap = MapBuilder.<String, String> create()
			.put("pid", "b")
			.put("id", "c")
			.build();
		map.putAllNode(Arrays.asList(aMap, bMap, cMap), m -> m.get("id"), m -> m.get("pid"), true);

		final TreeEntry<String, Map<String, String>> a = map.get("a");
		Assert.notNull(a);
		final TreeEntry<String, Map<String, String>> b = map.get("b");
		Assert.notNull(b);
		final TreeEntry<String, Map<String, String>> c = map.get("c");
		Assert.notNull(c);

		Assert.isNull(a.getDeclaredParent());
		Assert.equals(a, b.getDeclaredParent());
		Assert.equals(b, c.getDeclaredParent());

		Assert.equals(aMap, a.getValue());
		Assert.equals(bMap, b.getValue());
		Assert.equals(cMap, c.getValue());
	}

	@Test
	public void putNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		Assert.isNull(map.get("a"));

		map.putNode("a", "aaa");
		Assert.notNull(map.get("a"));
		Assert.equals("aaa", map.get("a").getValue());

		map.putNode("a", "aaaa");
		Assert.notNull(map.get("a"));
		Assert.equals("aaaa", map.get("a").getValue());
	}

	@Test
	public void putLinkedNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		Assert.isNull(map.get("a"));
		Assert.isNull(map.get("b"));

		map.putLinkedNodes("a", "b", "bbb");
		Assert.notNull(map.get("a"));
		Assert.isNull(map.get("a").getValue());
		Assert.notNull(map.get("b"));
		Assert.equals("bbb", map.get("b").getValue());

		map.putLinkedNodes("a", "b", "bbbb");
		Assert.notNull(map.get("a"));
		Assert.isNull(map.get("a").getValue());
		Assert.notNull(map.get("b"));
		Assert.equals("bbbb", map.get("b").getValue());
	}

	@Test
	public void putLinkedNodesTest2() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		Assert.isNull(map.get("a"));
		Assert.isNull(map.get("b"));

		map.putLinkedNodes("a", "aaa", "b", "bbb");
		Assert.notNull(map.get("a"));
		Assert.equals("aaa", map.get("a").getValue());
		Assert.notNull(map.get("b"));
		Assert.equals("bbb", map.get("b").getValue());

		map.putLinkedNodes("a", "aaaa", "b", "bbbb");
		Assert.notNull(map.get("a"));
		Assert.equals("aaaa", map.get("a").getValue());
		Assert.notNull(map.get("b"));
		Assert.equals("bbbb", map.get("b").getValue());
	}

	@Test
	public void linkNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");

		final TreeEntry<String, String> parent = map.get("a");
		final TreeEntry<String, String> child = map.get("b");

		Assert.notNull(parent);
		Assert.equals("a", parent.getKey());
		Assert.equals(child, parent.getChild("b"));

		Assert.notNull(child);
		Assert.equals("b", child.getKey());
		Assert.equals(parent, child.getDeclaredParent());
	}

	@Test
	public void unlinkNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		final TreeEntry<String, String> parent = map.get("a");
		final TreeEntry<String, String> child = map.get("b");
		map.unlinkNode("a", "b");
		Assert.isFalse(child.hasParent());
		Assert.isFalse(parent.hasChildren());
	}

	@Test
	public void getTreeNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final List<String> expected = CollUtil.newArrayList("a", "b", "c");
		List<String> actual = CollStreamUtil.toList(map.getTreeNodes("a"), TreeEntry::getKey);
		Assert.equals(expected, actual);
		actual = CollStreamUtil.toList(map.getTreeNodes("b"), TreeEntry::getKey);
		Assert.equals(expected, actual);
		actual = CollStreamUtil.toList(map.getTreeNodes("c"), TreeEntry::getKey);
		Assert.equals(expected, actual);
	}

	@Test
	public void getRootNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> root = map.get("a");
		Assert.equals(root, map.getRootNode("a"));
		Assert.equals(root, map.getRootNode("b"));
		Assert.equals(root, map.getRootNode("c"));
	}

	@Test
	public void getDeclaredParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		Assert.equals(a, map.getDeclaredParentNode("b"));
		Assert.equals(b, map.getDeclaredParentNode("c"));
	}

	@Test
	public void getParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");

		Assert.equals(a, map.getParentNode("c", "a"));
		Assert.equals(b, map.getParentNode("c", "b"));
		Assert.equals(a, map.getParentNode("b", "a"));
		Assert.isNull(map.getParentNode("a", "a"));
	}

	@Test
	public void containsParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		Assert.isTrue(map.containsParentNode("c", "b"));
		Assert.isTrue(map.containsParentNode("c", "a"));
		Assert.isTrue(map.containsParentNode("b", "a"));
		Assert.isFalse(map.containsParentNode("a", "a"));
	}

	@Test
	public void containsChildNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		Assert.isTrue(map.containsChildNode("a", "b"));
		Assert.isTrue(map.containsChildNode("a", "c"));
		Assert.isTrue(map.containsChildNode("b", "c"));
		Assert.isFalse(map.containsChildNode("c", "c"));
	}

	@Test
	public void getDeclaredChildNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		Assert.equals(CollUtil.newArrayList(b), new ArrayList<>(map.getDeclaredChildNodes("a")));
		Assert.equals(CollUtil.newArrayList(c), new ArrayList<>(map.getDeclaredChildNodes("b")));
		Assert.equals(CollUtil.newArrayList(), new ArrayList<>(map.getDeclaredChildNodes("c")));
	}

	@Test
	public void getChildNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		Assert.equals(CollUtil.newArrayList(b, c), new ArrayList<>(map.getChildNodes("a")));
		Assert.equals(CollUtil.newArrayList(c), new ArrayList<>(map.getChildNodes("b")));
		Assert.equals(CollUtil.newArrayList(), new ArrayList<>(map.getChildNodes("c")));
	}

}
