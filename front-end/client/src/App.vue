<template>
  <div id="app">
    <h1>My Bookstore</h1>
    <Bookstore/>
  </div>
</template>

<script>
import Bookstore from './components/Bookstore.vue'

export default {
  name: 'App',
  components: {
    Bookstore
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  color: #2c3e50;
  margin-top: 60px;
}
</style>
