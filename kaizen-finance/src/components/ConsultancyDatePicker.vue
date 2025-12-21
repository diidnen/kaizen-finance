<template>
  <div v-if="isConsultancyService" class="date-picker-container">
    <el-date-picker
      v-model="dateValue"
      type="datetime"
      placeholder="Select start time"
      :disabled-date="disabledDate"
      :disabled-hours="disabledHours"
      format="YYYY-MM-DD HH:mm"
      value-format="YYYY-MM-DD HH:mm"
      @change="handleStartTimeChange"
      style="width: 100%; margin-bottom: 10px;"
    />
    <el-date-picker
      v-model="endTimeValue"
      type="datetime"
      placeholder="Select end time"
      :disabled-date="disabledDate"
      :disabled-hours="disabledHours"
      format="YYYY-MM-DD HH:mm"
      value-format="YYYY-MM-DD HH:mm"
      @change="handleEndTimeChange"
      style="width: 100%; margin-bottom: 10px;"
    />
    <el-input
      v-model="estimateTime"
      type="text"
      placeholder="Estimate time (e.g., 30 mins, 1 hour)"
      @input="handleEstimateTimeChange"
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
      dateValue: null,
      endTimeValue: null,
      estimateTime: ''
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
    handleStartTimeChange(time) {
      this.dateValue = time
      this.$emit('change', this.serviceId, time, this.endTimeValue, this.estimateTime)
    },
    handleEndTimeChange(time) {
      this.endTimeValue = time
      this.$emit('change', this.serviceId, this.dateValue, time, this.estimateTime)
    },
    handleEstimateTimeChange() {
      this.$emit('change', this.serviceId, this.dateValue, this.endTimeValue, this.estimateTime)
    }
  }
}
</script>

<style scoped>
.date-picker-container {
  margin-bottom: 1rem;
}
</style> 