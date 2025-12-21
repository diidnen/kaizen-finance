<template>
  <div class="pricing-container"> 
    <h1 class="main-title">Service Price List 2024</h1>
    
    <!-- Service Categories -->
    <div class="service-categories">
      <button 
        v-for="(category, index) in categories" 
        :key="index"
        :class="['category-btn', { active: currentCategory === index }]"
        @click="currentCategory = index"
      >
        {{ category.name }}
      </button>
    </div>

    <!-- Service Selection -->
    <div class="services-list">
      <div v-for="service in currentServices" :key="service.id" class="service-item">
        <div class="service-header">
          <h3 class="service-name">{{ service.name }}</h3>
          <div class="service-description" v-if="service.description">
            {{ service.description }}
          </div>
        </div>

        <div class="service-pricing">
          <!-- Price Option Container -->
          <div v-if="service.hasMonthly || service.hasYearly || service.hasFixed">
            <!-- Date Picker (shown only for consultancy services) -->
            <consultancy-date-picker
              :service-id="service.id"
              @change="handleTimeChange"
            />

            <!-- Monthly Pricing -->
            <div v-if="service.hasMonthly" class="price-option">
              <label class="price-checkbox">
                <input 
                  type="checkbox" 
                  v-model="selectedServices" 
                  :value="{id: service.id, type: 'monthly'}"
                  @change="updateQuote"
                >
                <div class="price-details">
                  <span class="price-label">Per Month (Exc VAT)</span>
                  <span class="price-amount">£{{ service.monthlyPrice.toFixed(2) }}</span>
                </div>
              </label>
            </div>

            <!-- Yearly Pricing -->
            <div v-if="service.hasYearly" class="price-option">
              <label class="price-checkbox">
                <input 
                  type="checkbox" 
                  v-model="selectedServices" 
                  :value="{id: service.id, type: 'yearly'}"
                  @change="updateQuote"
                >
                <div class="price-details">
                  <span class="price-label">Per Year (Exc VAT)</span>
                  <span class="price-amount">£{{ service.yearlyPrice.toFixed(2) }}</span>
                </div>
              </label>
            </div>

            <!-- Fixed Price -->
            <div v-if="service.hasFixed" class="price-option">
              <label class="price-checkbox">
                <input 
                  type="checkbox" 
                  v-model="selectedServices" 
                  :value="{id: service.id, type: 'fixed'}"
                  @change="updateQuote"
                >
                <div class="price-details">
                  <!-- Time selection for consultancy services -->
                  <div v-if="service.hasTimeSelection && (service.id === 'consult-offsite' || service.id === 'consult-onsite')" class="time-selection">
                    <label class="time-label">Select Duration:</label>
                    <select 
                      v-model="service.selectedTimeUnits" 
                      @change="updateServicePrice(service)"
                      class="time-select"
                    >
                      <option v-for="units in getTimeUnits(service)" :key="units" :value="units">
                        {{ formatTimeUnits(units) }}
                      </option>
                    </select>
                  </div>
                  
                  <!-- Time selection for telephone services -->
                  <div v-if="service.hasTimeSelection && (service.id === 'comm-call-local' || service.id === 'comm-call-intl')" class="time-selection">
                    <label class="time-label">Select Minutes:</label>
                    <select 
                      v-model="service.selectedMinutes" 
                      @change="updateServicePrice(service)"
                      class="time-select"
                    >
                      <option v-for="mins in getMinuteOptions()" :key="mins" :value="mins">
                        {{ mins }} {{ mins === 1 ? 'minute' : 'minutes' }}
                      </option>
                    </select>
                    <label class="opt-in-checkbox" style="margin-top: 0.5rem; display: block;">
                      <input 
                        type="checkbox" 
                        v-model="service.optInTelephone"
                        @change="updateQuote"
                      >
                      <span>Opt in for telephone services</span>
                    </label>
                  </div>
                  
                  <span class="price-label">Price (Exc VAT)</span>
                  <span class="price-amount">
                    £{{ service ? getServicePrice(service).toFixed(2) : '0.00' }}
                    <span v-if="service && service.priceNote" class="price-note">{{ service.priceNote }}</span>
                  </span>
                </div>
              </label>
            </div>
          </div>

          <!-- Enquiry Only -->
          <div v-if="service.isEnquiryOnly" class="enquiry-option">
            <label class="price-checkbox">
              <input 
                type="checkbox" 
                v-model="selectedServices" 
                :value="{id: service.id, type: 'enquiry'}"
                @change="sendEnquiry(service)"
              >
              <div class="price-details">
                <span class="enquiry-text">{{ service.enquiryText }}</span>
              </div>
            </label>
          </div>
        </div>
      </div>
    </div>

    <!-- Quote Summary -->
    <div v-if="selectedServices.length > 0" class="quote-summary">
      <div class="quote-header">
        <h2>Your Quote Summary</h2>
        <div class="quote-reference">
          <strong>Quote Reference:</strong> 
          <span class="reference-number">{{ quoteReference || 'Not generated yet' }}</span>
          <button class="generate-ref-btn" @click="generateQuoteReference" v-if="!quoteReference">
            Generate Reference
          </button>
        </div>
      </div>
      <div v-if="!quoteReference" class="quote-warning">
        <i class="fas fa-exclamation-triangle"></i>
        <span>Please generate a Quote Reference before submitting your quote.</span>
      </div>
      
      <!-- 有价格的服务 -->
      <div v-if="pricedServices.length > 0" class="summary-table">
        <div class="summary-header">
          <span>Service</span>
          <span>Type</span>
          <span>Start Time</span>
          <span>End Time</span>
          <span>Estimate Time</span>
          <span>Price (Exc VAT)</span>
        </div>
        <div v-for="item in pricedServices" :key="`${item.id}-${item.type}`" class="summary-row">
          <span class="service-name">{{ item.name }}</span>
          <span class="service-type">{{ item.type }}</span>
          <span class="service-time">{{ item.startTime || '-' }}</span>
          <span class="service-time">{{ item.endTime || '-' }}</span>
          <span class="service-time">{{ item.estimateTime || '-' }}</span>
          <span class="price">£{{ item.priceExVat.toFixed(2) }}</span>
        </div>
        <div class="summary-total">
          <span>Total</span>
          <span></span>
          <span></span>
          <span></span>
          <span></span>
          <span>£{{ totals.exVat.toFixed(2) }}</span>
        </div>
      </div>

      <!-- 询价服务 -->
      <div v-if="enquiryServices.length > 0" class="enquiry-summary">
        <h3>Services Requiring Enquiry</h3>
        <div v-for="item in enquiryServices" :key="item.id" class="enquiry-row">
          <span class="service-name">{{ item.name }}</span>
          <div class="enquiry-details">
            <span class="enquiry-status">{{ item.enquiryText }}</span>
            <span v-if="item.time" class="enquiry-time">
              Scheduled: {{ item.time }}
            </span>
          </div>
        </div>
      </div>

      <!-- Special Notes -->
      <div class="special-notes">
        <h3>Important Notes</h3>
        <ul>
          <li v-for="(note, index) in notes" :key="index">{{ note }}</li>
        </ul>
      </div>

      <!-- Contact Information -->
      <div class="contact-info">
        <h3>Contact Us to Confirm Your Quote</h3>
        <div class="contact-details">
          <p><strong>Contact:</strong> Zhibing Mao</p>
          <p><strong>Tel:</strong> <a href="tel:+447599144489">+44 7599 144489</a></p>
          <p><strong>Email:</strong> <a href="mailto:info@kaizensolution.co.uk">info@kaizensolution.co.uk</a></p>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="action-buttons">
        <button class="generate-csv-btn" @click="generateCSV" v-if="selectedServices.length > 0 && quoteReference">
          <i class="fas fa-file-csv"></i>
          Generate Quote CSV
        </button>
        <button class="generate-pdf-btn" @click="generatePDF" v-if="selectedServices.length > 0 && quoteReference">
          <i class="fas fa-file-pdf"></i>
          Generate Quote PDF
        </button>
        <button 
          class="send-enquiry-btn" 
          @click="submitQuote"
          :disabled="!quoteReference"
          :class="{ 'disabled-btn': !quoteReference }"
        >
          <i class="fas fa-envelope"></i>
          Submit Quote
        </button>
      </div>
    </div>

    <!-- Add submit button -->
    
  </div>
</template>

<script>
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'
import { ref } from 'vue'
import { http, auth } from '@/utils/request'
import { ElMessage } from 'element-plus'
import ConsultancyDatePicker from './ConsultancyDatePicker.vue'

export default {
  name: 'Pricing',
  components: {
    ConsultancyDatePicker
  },
  data() {
    return {
      currentCategory: 0,
      selectedServices: ref([]),
      consultancyTimes: {}, // { serviceId: { startTime: '', endTime: '', estimateTime: '' } }
      quoteReference: '',
      telephoneOptIn: false,
      categories: [
        {
          name: 'START-UP AND PACKAGE OFFER',
          services: [
            {
              id: 'bp1',
              name: 'Business Plan (Basic)',
              description: 'Start-up SME service',
              hasFixed: true,
              fixedPrice: 1500,
              yearlyPrice: 1800,
              vatRate: 0.20
            },
            {
              id: 'setup1',
              name: 'Set-up of Company (Basic)',
              description: 'Start-up SME service',
              hasFixed: true,
              fixedPrice: 250,
              yearlyPrice: 300,
              vatRate: 0.20
            },
            {
              id: 'annual1',
              name: 'Annual Account Service Package',
              description: 'Start-up small business first year offer',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 125,
              monthlyPriceIncVat: 150,
              yearlyPrice: 1500,
              yearlyPriceIncVat: 1800,
              vatRate: 0.20
            },
            {
              id: 'bookkeeping1',
              name: 'Book-keeping Fix Cost Offer',
              description: 'Start-up small business first year offer',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 104,
              monthlyPriceIncVat: 125,
              yearlyPrice: 1250,
              yearlyPriceIncVat: 1500,
              vatRate: 0.20
            },
            {
              id: 'webdesign1',
              name: 'Web Page Design',
              description: 'Start-up small business first year offer',
              hasFixed: true,
              fixedPrice: 1900,
              yearlyPriceIncVat: 2280,
              vatRate: 0.20
            },
            {
              id: 'paperwork1',
              name: 'General Paper Work Review',
              description: '(+3rd party Communication fee 10% Disc)',
              hasFixed: true,
              fixedPrice: 1500,
              yearlyPriceIncVat: 1800,
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'FINANCIAL SERVICE',
          services: [
            {
              id: 'annual-sole',
              name: 'Annual Account - Sole Trader',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 75,
              monthlyPriceIncVat: 90,
              yearlyPrice: 900,
              yearlyPriceIncVat: 1080,
              vatRate: 0.20
            },
            {
              id: 'annual-small',
              name: 'Annual Account - Company <£61K turn over',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 125,
              monthlyPriceIncVat: 150,
              yearlyPrice: 1500,
              yearlyPriceIncVat: 1800,
              vatRate: 0.20
            },
            {
              id: 'annual-large',
              name: 'Annual Account - Company >£61K turn over',
              description: 'from',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 150,
              monthlyPriceIncVat: 180,
              yearlyPrice: 1800,
              yearlyPriceIncVat: 2160,
              vatRate: 0.20
            },
            {
              id: 'tax-return',
              name: 'Tax Return',
              hasFixed: true,
              fixedPrice: 295,
              priceNote: 'per return',
              vatRate: 0.20
            },
            {
              id: 'vat-return',
              name: 'VAT Return',
              hasFixed: true,
              fixedPrice: 425,
              priceNote: 'per return',
              vatRate: 0.20
            },
            {
              id: 'closing',
              name: 'Closing Company',
              description: '(excl account & third party fees)',
              hasFixed: true,
              fixedPrice: 485,
              priceNote: 'from',
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'PAYROLL',
          services: [
            {
              id: 'payroll-employee',
              name: 'Per Employees',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 24,
              yearlyPrice: 285,
              priceNote: 'from',
              vatRate: 0.20
            },
            {
              id: 'pension-plan',
              name: 'Pension Plan Per Employee',
              description: '(exclude External cost)',
              hasFixed: true,
              fixedPrice: 485,
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'BOOK-KEEPING',
          services: [
            {
              id: 'bookkeeping-35',
              name: 'Up to 35 Invoice',
              hasFixed: true,
              fixedPrice: 350,
              vatRate: 0.20
            },
            {
              id: 'bookkeeping-70',
              name: 'Up to 70 Invoice',
              hasFixed: true,
              fixedPrice: 490,
              vatRate: 0.20
            },
            {
              id: 'accounting-service',
              name: 'Accounting Service',
              description: '(e.g. Credit controls, cash flow forecast, management information (MI) and MI system development)',
              isEnquiryOnly: true,
              enquiryText: 'Please ask for price'
            }
          ]
        },
        {
          name: 'CONSULTANCY & COMMUNICATIONS',
          services: [
            {
              id: 'consult-offsite',
              name: 'Consultancy Fee Per Session (30 mins) - Off Site',
              hasFixed: true,
              fixedPrice: 250,
              pricePer30Min: 250, // 每30分钟的价格
              hasTimeSelection: true, // 启用时间选择
              selectedTimeUnits: 1, // 默认1个单位（30分钟）
              vatRate: 0.20
            },
            {
              id: 'consult-onsite',
              name: 'Consultancy Fee - On Client Site',
              description: '(min 1 hour)',
              hasFixed: true,
              fixedPrice: 295,
              pricePer30Min: 295, // 每30分钟的价格
              hasTimeSelection: true, // 启用时间选择
              selectedTimeUnits: 2, // 默认2个单位（1小时，最小）
              vatRate: 0.20
            },
            {
              id: 'comm-letter',
              name: 'Letter per page/email',
              hasFixed: true,
              fixedPrice: 55,
              vatRate: 0.20
            },
            {
              id: 'comm-call-local',
              name: 'Telephone call per min (Local)',
              hasFixed: true,
              fixedPrice: 5,
              pricePerMin: 5, // 每分钟的价格
              hasTimeSelection: true, // 启用时间选择
              selectedMinutes: 1, // 默认1分钟
              vatRate: 0.20
            },
            {
              id: 'comm-call-intl',
              name: 'Telephone call per min (International)',
              hasFixed: true,
              fixedPrice: 7.50,
              pricePerMin: 7.50, // 每分钟的价格
              hasTimeSelection: true, // 启用时间选择
              selectedMinutes: 1, // 默认1分钟
              vatRate: 0.20
            },
            {
              id: 'comm-copy',
              name: 'Photo copy',
              hasFixed: true,
              fixedPrice: 5,
              vatRate: 0.20
            },
            {
              id: 'comm-post',
              name: 'Postage service',
              hasFixed: true,
              fixedPrice: 7,
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'OTHER SERVICES',
          services: [
            {
              id: 'virtual-office',
              name: 'Virtual Office',
              hasMonthly: true,
              monthlyPrice: 132,
              vatRate: 0.20
            },
            {
              id: 'virtual-secretary',
              name: 'Virtual Secretary',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'marketing',
              name: 'Marketing and Web page design',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'it-solution',
              name: 'IT Solution',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'business-dev',
              name: 'Business Development & Consultancy',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'training',
              name: 'Training - Computing and Finance',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'legal',
              name: 'Legal (referral)',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'property-finance',
              name: 'Property and Business financing',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'fx-transfer',
              name: 'FX transfer',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            }
          ]
        },
        {
          name: 'VIRTUAL SECRETARY & OTHERS',
          services: [
            {
              id: 'virtual-secretary',
              name: 'Virtual Secretary Service',
              description: 'Professional virtual assistance and administrative support',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire for pricing'
            },
            {
              id: 'credit-control',
              name: 'Credit Controls',
              description: 'Cash flow forecast, management information (MI) and MI system development',
              isEnquiryOnly: true,
              enquiryText: 'Please ask for price'
            },
            {
              id: 'consultancy',
              name: 'Consultancy Service',
              description: 'Off Site: £250 (Exc VAT) / £300 (Inc VAT)\nOn Client Site: £295 (Exc VAT) / £354 (Inc VAT) (min 1 hour)',
              isEnquiryOnly: true,
              enquiryText: 'Per session (30 mins)'
            }
          ]
        }
      ],
      notes: [
        '* 15% discount if Book-keeping service have been taken',
        '** All members will receive one free consultancy session',
        '^ 5 minutes will be charge the full sessions. If on-site, cost and traveling time will be added'
      ]
    }
  },
  computed: {
    currentServices() {
      return this.categories[this.currentCategory].services
    },
    selectedServiceDetails() {
      return this.selectedServices.map(item => {
        const service = this.findService(item.id)
        let priceExVat = 0, vat = 0, priceIncVat = 0

        if (!service.isEnquiryOnly) {
          switch(item.type) {
            case 'monthly':
              priceExVat = service.monthlyPrice
              break
            case 'yearly':
              priceExVat = service.yearlyPrice
              break
            case 'fixed':
              // 如果服务有时间选择，使用计算后的价格
              if (service.hasTimeSelection) {
                priceExVat = this.getServicePrice(service)
              } else {
                priceExVat = service.fixedPrice
              }
              break
          }
          vat = priceExVat * service.vatRate
          priceIncVat = priceExVat * (1 + service.vatRate)
        }

        const timeInfo = this.consultancyTimes[service.id] || {}
        return {
          id: service.id,
          name: service.name,
          type: item.type,
          priceExVat,
          isEnquiryOnly: service.isEnquiryOnly,
          enquiryText: service.enquiryText,
          startTime: timeInfo.startTime || '',
          endTime: timeInfo.endTime || '',
          estimateTime: timeInfo.estimateTime || ''
        }
      })
    },
    pricedServices() {
      return this.selectedServiceDetails.filter(s => !s.isEnquiryOnly)
    },
    enquiryServices() {
      return this.selectedServiceDetails.filter(s => s.isEnquiryOnly)
    },
    totals() {
      return this.pricedServices.reduce((acc, item) => ({
        exVat: acc.exVat + item.priceExVat
      }), { exVat: 0 })
    }
  },
  methods: {
    findService(id) {
      for (const category of this.categories) {
        const service = category.services.find(s => s.id === id)
        if (service) return service
      }
      return null
    },
    updateQuote() {
      // Handle quote updates
    },
    getServicePrice(service) {
      if (service && service.hasTimeSelection) {
        if (service.id === 'consult-offsite' || service.id === 'consult-onsite') {
          // 咨询费：按30分钟单位计算
          return (service.pricePer30Min || 0) * (service.selectedTimeUnits || 1)
        } else if (service.id === 'comm-call-local' || service.id === 'comm-call-intl') {
          // 电话费：按分钟计算
          return (service.pricePerMin || 0) * (service.selectedMinutes || 1)
        }
      }
      return service ? (service.fixedPrice || 0) : 0
    },
    updateServicePrice(service) {
      // 更新服务价格
      if (service) {
        service.fixedPrice = this.getServicePrice(service)
        this.updateQuote()
      }
    },
    getTimeUnits(service) {
      // 生成时间单位选项（30分钟为单位）
      // Off Site: 1-20个单位（30分钟到10小时）
      // On Site: 2-20个单位（1小时到10小时，最小1小时）
      if (!service) return []
      const minUnits = service.id === 'consult-onsite' ? 2 : 1
      const maxUnits = 20
      const units = []
      for (let i = minUnits; i <= maxUnits; i++) {
        units.push(i)
      }
      return units
    },
    formatTimeUnits(units) {
      // 格式化时间单位显示
      const hours = units / 2
      if (hours === 1) {
        return '1 hour (30 mins)'
      } else if (hours % 1 === 0) {
        return `${hours} hours (${units * 30} mins)`
      } else {
        return `${units * 30} mins`
      }
    },
    getMinuteOptions() {
      // 生成分钟选项：1-120分钟
      const options = []
      for (let i = 1; i <= 120; i++) {
        options.push(i)
      }
      return options
    },
    async generateQuoteReference() {
      try {
        const response = await http.get('/api/generate-quote-reference')
        if (response.code === 200) {
          this.quoteReference = response.quoteReference
          ElMessage.success('Quote Reference generated successfully')
        } else {
          ElMessage.error('Failed to generate quote reference')
        }
      } catch (error) {
        console.error('Error generating quote reference:', error)
        ElMessage.error('Failed to generate quote reference')
      }
    },
    generateCSV() {
      if (!this.quoteReference) {
        ElMessage.warning('Please generate a quote reference first')
        return
      }
      
      const username = auth.getUsername() || 'Guest'
      const quoteDate = new Date().toLocaleDateString('en-GB')
      
      // CSV头部
      const headers = [
        'Quote Reference',
        'Date',
        'Customer',
        'Service',
        'Type',
        'Start Time',
        'End Time',
        'Estimate Time',
        'Price (Exc VAT)'
      ]
      
      // 构建CSV数据
      const rows = []
      
      // 添加有价格的服务
      this.pricedServices.forEach(item => {
        rows.push([
          this.quoteReference,
          quoteDate,
          username,
          item.name,
          item.type,
          item.startTime || '',
          item.endTime || '',
          item.estimateTime || '',
          `£${item.priceExVat.toFixed(2)}`
        ])
      })
      
      // 添加询价服务
      this.enquiryServices.forEach(service => {
        const timeInfo = this.consultancyTimes[service.id] || {}
        rows.push([
          this.quoteReference,
          quoteDate,
          username,
          service.name,
          'Enquiry',
          timeInfo.startTime || '',
          timeInfo.endTime || '',
          timeInfo.estimateTime || '',
          service.enquiryText || 'Enquiry Required'
        ])
      })
      
      // 添加总计行
      rows.push([
        this.quoteReference,
        quoteDate,
        username,
        'TOTAL',
        '',
        '',
        '',
        '',
        `£${this.totals.exVat.toFixed(2)}`
      ])
      
      // 转换为CSV格式
      const csvContent = [
        headers.join(','),
        ...rows.map(row => row.map(cell => {
          // 处理包含逗号或引号的单元格
          if (typeof cell === 'string' && (cell.includes(',') || cell.includes('"') || cell.includes('\n'))) {
            return `"${cell.replace(/"/g, '""')}"`
          }
          return cell
        }).join(','))
      ].join('\n')
      
      // 添加BOM以支持Excel正确显示UTF-8字符
      const BOM = '\uFEFF'
      const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' })
      
      // 创建下载链接
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `kaizen-solution-quote-${this.quoteReference}.csv`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      
      ElMessage.success('CSV file generated successfully')
    },
    async generatePDF() {
      const doc = new jsPDF()
      const pageWidth = doc.internal.pageSize.getWidth()
      const pageHeight = doc.internal.pageSize.getHeight()
      
      // 使用已生成的Quote Reference，如果没有则生成一个
      if (!this.quoteReference) {
        this.generateQuoteReference()
      }
      const quoteNumber = this.quoteReference
      const quoteDate = new Date().toLocaleDateString('en-GB', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
      })
      
      // 公司信息（顶部左侧）
      doc.setFontSize(18)
      doc.setFont(undefined, 'bold')
      doc.text('Kaizen Finance Solution', 14, 20)
      
      doc.setFontSize(10)
      doc.setFont(undefined, 'normal')
      doc.text('London, UK', 14, 27)
      doc.text('Tel: +44 7599 144489', 14, 32)
      doc.text('Email: info@kaizensolution.co.uk', 14, 37)
      
      // Quote信息（顶部右侧）
      doc.setFontSize(16)
      doc.setFont(undefined, 'bold')
      doc.text('QUOTE', pageWidth - 14, 20, { align: 'right' })
      
      doc.setFontSize(10)
      doc.setFont(undefined, 'normal')
      doc.text(`Quote Reference: ${quoteNumber}`, pageWidth - 14, 27, { align: 'right' })
      doc.text(`Date: ${quoteDate}`, pageWidth - 14, 32, { align: 'right' })
      
      // 客户信息（如果有）
      const username = auth.getUsername()
      let currentY = 50
      if (username) {
        doc.setFontSize(12)
        doc.setFont(undefined, 'bold')
        doc.text('Bill To:', 14, currentY)
        currentY += 7
        doc.setFontSize(10)
        doc.setFont(undefined, 'normal')
        doc.text(username, 14, currentY)
        currentY += 10
      }
      
      // 分隔线
      doc.setDrawColor(200, 200, 200)
      doc.line(14, currentY, pageWidth - 14, currentY)
      currentY += 15
      
      // 分开处理有价格和询价的服务
      const pricedServices = this.selectedServiceDetails.filter(s => !s.isEnquiryOnly)
      const enquiryServices = this.selectedServiceDetails.filter(s => s.isEnquiryOnly)
      
      // 添加有价格的服务表格
      if (pricedServices.length > 0) {
        const tableData = pricedServices.map(item => [
          item.name,
          item.type,
          item.startTime || '-',
          item.endTime || '-',
          item.estimateTime || '-',
          `£${item.priceExVat.toFixed(2)}`
        ])
        
        // 使用 autoTable 插件生成表格
        autoTable(doc, {
          startY: currentY,
          head: [['Service', 'Type', 'Start Time', 'End Time', 'Estimate Time', 'Price (Exc VAT)']],
          body: tableData,
          foot: [[
            'Total',
            '',
            '',
            '',
            '',
            `£${this.totals.exVat.toFixed(2)}`
          ]],
          styles: {
            fontSize: 9,
            cellPadding: 3
          },
          headStyles: {
            fillColor: [27, 54, 93], // #1B365D
            textColor: 255,
            fontStyle: 'bold'
          },
          footStyles: {
            fillColor: [248, 249, 250], // #f8f9fa
            textColor: 0,
            fontStyle: 'bold'
          },
          columnStyles: {
            0: { cellWidth: 50 }, // Service name
            1: { cellWidth: 20 }, // Type
            2: { cellWidth: 25 }, // Start Time
            3: { cellWidth: 25 }, // End Time
            4: { cellWidth: 25 }, // Estimate Time
            5: { cellWidth: 20, halign: 'right' }  // Price Exc VAT
          },
          margin: { left: 14, right: 14 }
        })
        
        currentY = doc.lastAutoTable.finalY + 20
      }
      
      // 添加询价服务列表
      if (enquiryServices.length > 0) {
        doc.text('Services Requiring Enquiry:', 14, currentY)
        currentY += 10
        
        enquiryServices.forEach(service => {
          doc.text(`• ${service.name}`, 20, currentY)
          const timeInfo = this.consultancyTimes[service.id] || {}
          if (timeInfo.startTime || timeInfo.endTime || timeInfo.estimateTime) {
            currentY += 7
            doc.setFontSize(9)
            if (timeInfo.startTime) {
              doc.text(`  Start Time: ${timeInfo.startTime}`, 25, currentY)
              currentY += 6
            }
            if (timeInfo.endTime) {
              doc.text(`  End Time: ${timeInfo.endTime}`, 25, currentY)
              currentY += 6
            }
            if (timeInfo.estimateTime) {
              doc.text(`  Estimate Time: ${timeInfo.estimateTime}`, 25, currentY)
              currentY += 6
            }
            doc.setFontSize(10)
          }
          if (service.description) {
            currentY += 7
            doc.setFontSize(10)
            doc.text(service.description, 25, currentY)
            doc.setFontSize(12)
          }
          currentY += 10
        })
        
        currentY += 10
      }
      
      // 检查是否需要新页面
      if (currentY > pageHeight - 60) {
        doc.addPage()
        currentY = 20
      }
      
      // 添加注意事项
      doc.setFontSize(11)
      doc.setFont(undefined, 'bold')
      doc.text('Important Notes:', 14, currentY)
      currentY += 8
      
      doc.setFontSize(9)
      doc.setFont(undefined, 'normal')
      this.notes.forEach(note => {
        if (currentY > pageHeight - 20) {
          doc.addPage()
          currentY = 20
        }
        doc.text(note, 20, currentY)
        currentY += 7
      })
      
      // 添加付款条款
      currentY += 10
      if (currentY > pageHeight - 40) {
        doc.addPage()
        currentY = 20
      }
      
      doc.setFontSize(11)
      doc.setFont(undefined, 'bold')
      doc.text('Payment Terms:', 14, currentY)
      currentY += 8
      
      doc.setFontSize(9)
      doc.setFont(undefined, 'normal')
      doc.text('Please contact us to confirm this quote and arrange payment.', 20, currentY)
      currentY += 7
      doc.text('All prices are exclusive of VAT unless otherwise stated.', 20, currentY)
      
      // 添加联系信息（底部）
      currentY = pageHeight - 40
      doc.setDrawColor(200, 200, 200)
      doc.line(14, currentY, pageWidth - 14, currentY)
      currentY += 10
      
      doc.setFontSize(10)
      doc.setFont(undefined, 'bold')
      doc.text('Contact Information:', 14, currentY)
      currentY += 7
      
      doc.setFontSize(9)
      doc.setFont(undefined, 'normal')
      doc.text('Contact: Zhibing Mao', 14, currentY)
      currentY += 6
      doc.text('Tel: +44 7599 144489', 14, currentY)
      currentY += 6
      doc.text('Email: info@kaizensolution.co.uk', 14, currentY)
      
      // 保存 PDF
      doc.save(`kaizen-solution-quote-${quoteNumber}.pdf`)
    },
    async sendEnquiry(service) {
      try {
        const username = auth.getUsername()
        if (!username) {
          ElMessage.error('Please login first')
          return
        }

        // 检查服务是否已经在 selectedServices 中
        const existingIndex = this.selectedServices.findIndex(s => s.id === service.id)
        if (existingIndex === -1) {
          // 如果不在，添加到 selectedServices
          this.selectedServices.push({
            id: service.id,
            type: 'enquiry',
            time: service.id.includes('consult') ? null : undefined // 只为咨询服务初始化时间
          })
        }

        ElMessage.success('Service added to enquiry list')
        this.updateQuote()
      } catch (error) {
        console.error('Error adding enquiry service:', error)
        ElMessage.error('Failed to add service to enquiry list: ' + error.message)
      }
    },
    handleTimeChange(serviceId, startTime, endTime, estimateTime) {
      // 更新咨询时间
      this.consultancyTimes[serviceId] = {
        startTime: startTime || '',
        endTime: endTime || '',
        estimateTime: estimateTime || ''
      }
      
      this.updateQuote()
    },
    async submitQuote() {
      try {
        const username = auth.getUsername()
        if (!username) {
          ElMessage.error('Please login first')
          return
        }

        // 检查是否已生成 Quote Reference
        if (!this.quoteReference) {
          ElMessage.warning('Please generate a Quote Reference first before submitting your quote.')
          // 自动生成 reference
          this.generateQuoteReference()
          ElMessage.info('Quote Reference has been generated. Please click Submit Quote again.')
          return
        }

        // 构造所有服务数据，包括询价服务
        const timeInfo = (serviceId) => this.consultancyTimes[serviceId] || {}
        const services = this.selectedServiceDetails.map(service => {
          const times = timeInfo(service.id)
          return {
            name: service.name,
            price: service.isEnquiryOnly ? 'Enquiry Required' : service.priceExVat.toFixed(2),
            type: service.type,
            startTime: times.startTime || undefined,
            endTime: times.endTime || undefined,
            estimateTime: times.estimateTime || undefined
          }
        })

        const enquiryServices = this.enquiryServices.map(service => {
          const times = timeInfo(service.id)
          return {
            id: service.id,
            name: service.name,
            description: service.description,
            price: service.enquiryText,
            type: 'Enquiry',
            startTime: times.startTime || undefined,
            endTime: times.endTime || undefined,
            estimateTime: times.estimateTime || undefined
          }
        })

        // 如果没有quote reference，先生成一个
        if (!this.quoteReference) {
          this.generateQuoteReference()
        }
        
        const response = await http.post('/api/submit-order', {
          username: username,
          quoteReference: this.quoteReference,
          services: services,
          enquiryServices: enquiryServices,
          orderDate: new Date().toISOString()
        })

        

        if (response.code === 200) {
          ElMessage.success('Quote submitted successfully')
          // 先导航到order页面
          await this.$router.push('/order')
          // 等待导航完成后再刷新
          setTimeout(() => {
            window.location.reload()
          }, 100)
        } else {
          ElMessage.error('Failed to submit quote')
        }
      } catch (error) {
        console.error('Error submitting quote:', error)
        ElMessage.error('Failed to submit quote: ' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.pricing-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: Arial, sans-serif;
}

.main-title {
  color: #1B365D;
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2.5rem;
}

/* Category Navigation */
.service-categories {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 2rem;
}

.category-btn {
  padding: 1rem 1.5rem;
  background: #f8f9fa;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 600;
  color: #1B365D;
}

.category-btn.active {
  background: #1B365D;
  color: white;
}

/* Service Items */
.services-list {
  display: grid;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.service-item {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.service-header {
  margin-bottom: 1rem;
}

.service-name {
  font-size: 1.2rem;
  color: #1B365D;
  margin: 0;
  font-weight: 600;
}

.service-description {
  color: #666;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

/* Pricing Options */
.service-pricing {
  display: grid;
  gap: 1rem;
}

.price-option {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 4px;
}

.price-checkbox {
  display: flex;
  align-items: center;
  gap: 1rem;
  cursor: pointer;
}

.price-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  flex-grow: 1;
}

.price-label {
  color: #666;
  font-size: 0.9rem;
}

.time-selection {
  margin-bottom: 0.75rem;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.time-label {
  display: block;
  font-size: 0.9rem;
  color: #1B365D;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.time-select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.95rem;
  background: white;
  cursor: pointer;
  transition: border-color 0.3s;
}

.time-select:hover {
  border-color: #1B365D;
}

.time-select:focus {
  outline: none;
  border-color: #1B365D;
  box-shadow: 0 0 0 2px rgba(27, 54, 93, 0.1);
}

.price-amount {
  font-weight: 600;
  color: #1B365D;
}

.price-note {
  font-size: 0.8rem;
  color: #666;
  margin-left: 0.5rem;
}

/* Enquiry Option */
.enquiry-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.enquiry-text {
  color: #1B365D;
  font-style: italic;
}

.enquiry-btn {
  padding: 0.5rem 1rem;
  background: transparent;
  border: 1px solid #1B365D;
  color: #1B365D;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.enquiry-btn:hover {
  background: #1B365D;
  color: white;
}

/* Quote Summary */
.quote-summary {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-top: 2rem;
  margin-bottom: 2rem;
}

.quote-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #e9ecef;
}

.quote-reference {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
}

.reference-number {
  color: #1B365D;
  font-weight: 600;
  font-family: monospace;
  padding: 0.3rem 0.8rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.generate-ref-btn {
  padding: 0.4rem 0.8rem;
  background: #1B365D;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: all 0.3s ease;
}

.generate-ref-btn:hover {
  background: #0056b3;
}

.quote-warning {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem;
  margin: 1rem 0;
  background: #fff3cd;
  border: 1px solid #ffc107;
  border-radius: 4px;
  color: #856404;
  font-size: 0.95rem;
}

.quote-warning i {
  color: #ffc107;
  font-size: 1.2rem;
}

.disabled-btn {
  opacity: 0.6;
  cursor: not-allowed !important;
  pointer-events: none;
}

.summary-table {
  width: 100%;
  margin: 2rem 0;
}

.summary-header {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1.5fr 1.5fr 1fr;
  gap: 1.5rem;
  padding: 1.2rem 1rem;
  background: #f8f9fa;
  font-weight: 600;
  color: #1B365D;
}

.summary-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1.5fr 1.5fr 1fr;
  gap: 1.5rem;
  padding: 1.2rem 1rem;
  border-bottom: 1px solid #eee;
}

.summary-total {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1.5fr 1.5fr 1fr;
  gap: 1.5rem;
  padding: 1.2rem 1rem;
  font-weight: 600;
  background: #f8f9fa;
  color: #1B365D;
  margin-top: 0.5rem;
}

/* Special Notes */
.special-notes {
  margin: 2.5rem 0;
  padding: 1.8rem;
  background: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #1B365D;
}

.special-notes h3 {
  color: #1B365D;
  margin-bottom: 1rem;
}

.special-notes ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.special-notes li {
  margin: 0.5rem 0;
  color: #666;
}

/* Contact Information */
.contact-info {
  margin: 2.5rem 0;
  padding: 1.8rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.contact-info h3 {
  color: #1B365D;
  margin-bottom: 1rem;
}

.contact-details {
  display: grid;
  gap: 0.5rem;
}

.contact-details a {
  color: #1B365D;
  text-decoration: none;
}

.contact-details a:hover {
  text-decoration: underline;
}

/* Action Buttons */
.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 2rem;
  margin-bottom: 2rem;
  padding: 1rem 0;
  position: relative;
  z-index: 10;
}

.generate-pdf-btn,
.generate-csv-btn,
.send-enquiry-btn {
  padding: 1rem 1.5rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
  position: relative;
  z-index: 100;
  min-width: 180px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.generate-pdf-btn,
.generate-csv-btn {
  background: #1B365D;
  color: white;
}

.generate-csv-btn {
  background: #28a745;
}

.generate-csv-btn:hover {
  background: #218838;
}

.send-enquiry-btn {
  background: white;
  color: #1B365D;
  border: 2px solid #1B365D;
}

.generate-pdf-btn:hover {
  background: #2C5282;
}

.send-enquiry-btn:hover {
  background: #1B365D;
  color: white;
}

/* Responsive Design */
@media (max-width: 768px) {
  .pricing-container {
    padding: 1rem;
  }

  .service-categories {
    flex-direction: column;
  }

  .price-details {
    grid-template-columns: 1fr;
  }

  .summary-header,
  .summary-row,
  .summary-total {
    grid-template-columns: 1fr;
    text-align: left;
  }

  .action-buttons {
    grid-template-columns: 1fr;
  }

  .main-title {
    font-size: 2rem;
  }
}

/* Checkbox Custom Style */
input[type="checkbox"] {
  width: 20px;
  height: 20px;
  border: 2px solid #1B365D;
  border-radius: 4px;
  cursor: pointer;
}

input[type="checkbox"]:checked {
  background-color: #1B365D;
  border-color: #1B365D;
}

/* Print Styles */
@media print {
  .pricing-container {
    padding: 0;
  }

  .service-categories,
  .enquiry-btn,
  .action-buttons {
    display: none;
  }

  .quote-summary {
    box-shadow: none;
    padding: 0;
  }
}

.enquiry-summary {
  margin-top: 2rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.enquiry-row {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
}

.enquiry-status {
  color: #666;
  font-style: italic;
}

.enquiry-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.enquiry-time {
  font-size: 0.9em;
  color: #1B365D;
  font-weight: 500;
}

.price-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 调整时间选择器样式 */
:deep(.el-date-picker) {
  width: 100%;
}

.service-time {
  color: #1B365D;
  font-size: 0.9rem;
}
</style>