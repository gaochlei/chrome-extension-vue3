<template>
  <div class="dropdown" ref="dropdownRef" @click.prevent="toggleOpen">
    <slot name="drop_header">
      <a
        class="btn btn-outline-dark dropdown-menu-dark dropdown-toggle text-dark"
        style="background-color: #e3f2fd; min-width: 110px; max-width: 110px"
        >{{ name }}</a
      >
    </slot>
    <div style="width:70px">
      <ul
        class="dropdown-menu dropdown-menu-dark"
        aria-labelledby="dropdownMenuButton"
        :style="{ display: 'block' }"
        style="background-color: #ffffff"
        v-show="isOpen"
      >
        <slot name="drop_item"></slot>
      </ul>
    </div>
  </div>
</template>
<script>
import { defineComponent, ref, watch } from "vue";
import useClickOutside from "../../util/useClickOutside";
import store from "../../store";
// import mitt from "mitt";
// export const popMenuEmitter = mitt();
export default defineComponent({
  name: "Dropdown",
  props: {
    name: {
      type: ref,
      required: true,
    },
  },
  setup(props, context) {
    const userName = ref();
    userName.value = props.name;
    const isOpen = ref(false);
    const dropdownRef = ref();
    // const userName = ref();
    const toggleOpen = () => {
      isOpen.value = !isOpen.value;
    };
    const isClickOutside = useClickOutside(dropdownRef);
    watch(isClickOutside, () => {
      if (isClickOutside.value) {
        isOpen.value = false;
      } else {
        isOpen.value = true;
      }
    });

    return {
      isOpen,
      toggleOpen,
      dropdownRef,
      userName,
    };
  },
});
</script>
<style scoped></style>
