package org.example.oauth2.server;

import org.example.oauth2.server.conf.Oauth2UserInfoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestOauth2UserInfoMapper {
  
  private Oauth2UserInfoMapper userInfoMapper;
  
  @BeforeEach
  public void setUp() {
    userInfoMapper = new Oauth2UserInfoMapper();
  }
  
  private List<String> getNestedListForKey(Map<String, Object> input, String[] keys) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = Oauth2UserInfoMapper.class.getDeclaredMethod("getNestedListForKey", Map.class, String[].class);
    method.setAccessible(true);
    return (List<String>) method.invoke(userInfoMapper, input, keys);
  }
  
  @Test
  public void testGetNestedListForKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Map<String, Object> input = Map.of("resource_access", Map.of("oauth2-demo-client", Map.of("roles", List.of("USER2", "USER"))));
    String[] keys = {"resource_access", "oauth2-demo-client", "roles"};
    List<String> roles = getNestedListForKey(input, keys);
    List<String> expected = Arrays.asList("USER2", "USER");
    Assertions.assertEquals(expected, roles);
  }
  
  @Test
  public void firstMapEntryIsMissing() {
    Map<String, Object> input = Map.of("oauth2-demo-client", Map.of("roles", List.of("USER2", "USER")));
    String[] keys = {"resource_access", "oauth2-demo-client", "roles"};
    try {
      getNestedListForKey(input, keys);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      Throwable cause = e.getCause();
      Assertions.assertTrue(cause instanceof IllegalArgumentException);
      Assertions.assertEquals("Key 'resource_access' not found in the map.", cause.getMessage());
    }
  }
  
  @Test
  public void lastMapEntryIsMissing() {
    Map<String, Object> input = Map.of("resource_access", Map.of("oauth2-demo-client", Map.of("selor", List.of())));
    String[] keys = {"resource_access", "oauth2-demo-client", "roles"};
    try {
      getNestedListForKey(input, keys);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      Throwable cause = e.getCause();
      Assertions.assertTrue(cause instanceof IllegalArgumentException);
      Assertions.assertEquals("Key 'roles' not found in the map.", cause.getMessage());
    }
  }
  
  @Test
  public void firstKeyIsMissing() {
    Map<String, Object> input = Map.of("resource_access", Map.of("oauth2-demo-client", Map.of("roles", List.of("USER2", "USER"))));
    String[] keys = {"oauth2-demo-client", "roles"};
    try {
      getNestedListForKey(input, keys);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      Throwable cause = e.getCause();
      Assertions.assertTrue(cause instanceof IllegalArgumentException);
      Assertions.assertEquals("Key 'oauth2-demo-client' not found in the map.", cause.getMessage());
    }
  }
  
  @Test
  public void lastKeyIsMissing() {
    Map<String, Object> input = Map.of("resource_access", Map.of("oauth2-demo-client", Map.of("roles", List.of("USER2", "USER"))));
    String[] keys = {"resource_access", "oauth2-demo-client"};
    try {
      getNestedListForKey(input, keys);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      Throwable cause = e.getCause();
      Assertions.assertTrue(cause instanceof IllegalArgumentException);
      Assertions.assertEquals("Last key should contain a list or array of strings, but it does not.", cause.getMessage());
    }
  }
  
  @Test
  public void emptyKeys() {
    Map<String, Object> input = Map.of("resource_access", Map.of("oauth2-demo-client", Map.of("roles", List.of("USER2", "USER"))));
    String[] keys = {};
    try {
      getNestedListForKey(input, keys);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      Throwable cause = e.getCause();
      Assertions.assertTrue(cause instanceof IllegalArgumentException);
      Assertions.assertEquals("The keys array cannot be empty.", cause.getMessage());
    }
  }
  
  @Test
  public void rolesContainsNotList() {
    Map<String, Object> input = Map.of("resource_access", Map.of("oauth2-demo-client", Map.of("roles", "USER")));
    String[] keys = {"resource_access", "oauth2-demo-client", "roles"};
    try {
      getNestedListForKey(input, keys);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      Throwable cause = e.getCause();
      Assertions.assertTrue(cause instanceof IllegalArgumentException);
      Assertions.assertEquals("Last key should contain a list or array of strings, but it does not.", cause.getMessage());
    }
  }
}
