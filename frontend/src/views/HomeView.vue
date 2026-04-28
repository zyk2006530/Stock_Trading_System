<template>
  <section class="home">
    <div class="hero">
      <p class="eyebrow">Phase 1 Ready</p>
      <h1>Backend scaffolding is complete.</h1>
      <p class="lead">
        Start the backend, then wire up login and trading features in the next phase.
      </p>
      <el-button type="primary">Open Login</el-button>
    </div>
  </section>
</template>

<script setup>
</script>

<style scoped>
.home {
  display: flex;
  justify-content: center;
}

.hero {
  max-width: 720px;
  padding: 36px;
  background: rgba(255, 255, 255, 0.78);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(17, 24, 39, 0.12);
}

.eyebrow {
  font-size: 0.75rem;
  letter-spacing: 0.25em;
  text-transform: uppercase;
  color: rgba(31, 41, 55, 0.6);
}

.lead {
  margin: 16px 0 28px;
  font-size: 1rem;
  color: rgba(31, 41, 55, 0.7);
}
</style>
