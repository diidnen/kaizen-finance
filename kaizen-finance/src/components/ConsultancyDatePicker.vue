<template>
  <div v-if="isConsultancyService" class="date-picker-container">
    <el-date-picker
      v-model="dateValue"
      type="datetime"
      placeholder="Select consultation time"
      :disabled-date="disabledDate"
      :disabled-hours="disabledHours"
      format="YYYY-MM-DD HH:mm"
      value-format="YYYY-MM-DD HH:mm"
      @change="handleChange"
      style="width: 100%; margin-bottom: 10px;"
    />
  </div>
</template>

<script>
export default {
  name: 'ConsultancyDatePicker',
  props: {
    serviceId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      dateValue: null
    }
  },
  computed: {
    isConsultancyService() {
      return this.serviceId === 'consult-offsite' || this.serviceId === 'consult-onsite'
    }
  },
  methods: {
    disabledDate(time) {
      return time.getTime() < Date.now()
    },
    disabledHours() {
      // 禁用非工作时间 (before 8:00 and after 19:00)
      return Array.from(Array(24).keys()).filter(hour => hour < 8 || hour >= 19)
    },
    handleChange(time) {
      this.dateValue = time
      this.$emit('change', this.serviceId, time)
    }
  }
}
</script>

<style scoped>
.date-picker-container {
  margin-bottom: 1rem;
}
</style> 