<template>
  <div class="services-container">
    <h1 class="main-title">Our Services</h1>

    <!-- Core Services Section -->
    <section class="services-section">
      <h2>Core Business</h2>
      <div class="core-services">
        <div class="service-card">
          <i class="fas fa-calculator"></i>
          <h3>Accounting</h3>
          <p>Professional accounting services tailored to your business needs.</p>
        </div>
        <div class="service-card">
          <i class="fas fa-handshake"></i>
          <h3>Mortgage &amp; Commercial Finance</h3>
          <p>Sophisticated funding solutions for business and property financing, delivered with precision and integrity.</p>
        </div>
        <div class="service-card">
          <i class="fas fa-chart-line"></i>
          <h3>Consultancy Service</h3>
          <p>Expert business consultancy to help you plan, grow, and operate with confidence — plus Management Information Systems (MIS) that deliver clear, timely reporting so you can make smarter decisions.</p>
        </div>
      </div>
    </section>

    <!-- Additional Services Section -->
    <section class="services-section">
      <h2>Additional Services</h2>
      <div class="tabs">
        <div class="tab-buttons">
          <button
            v-for="(category, index) in categories"
            :key="index"
            :class="['tab-button', { active: currentCategory === index }]"
            @click="currentCategory = index"
          >
            {{ category.title }}
          </button>
        </div>

        <div class="tab-content">
          <!-- 1) Set up -->
          <div v-show="currentCategory === 0" class="tab-pane">
            <h3>Set up</h3>

            <div class="service-block">
              <p class="subtitle">Set up Company</p>
              <p>End-to-end support for UK company formation — from incorporation and registration to the essential steps needed to get your business up and running.</p>
            </div>

            <div class="service-block">
              <p class="subtitle">Virtual Office</p>
              <p>Virtual office with a central London address for businesses and directors.</p>
            </div>

            <div class="service-block">
              <p class="subtitle">Banking</p>
              <p>Strategic banking solutions to help you open and manage business accounts with leading providers.</p>
            </div>
          </div>

          <!-- 2) Finance -->
          <div v-show="currentCategory === 1" class="tab-pane">
            <h3>Finance</h3>

            <div class="service-block">
              <p class="subtitle">Commercial Finance</p>
              <p>Flexible commercial financing solutions through our trusted partners, including
                <a href="https://www.natwest.com/" target="_blank" class="company-link"><strong>NatWest</strong></a>,
                <a href="https://www.iwoca.co.uk/" target="_blank" class="company-link"><strong>IWOCA</strong></a>,
                and
                <a href="https://www.approvity.com/" target="_blank" class="company-link"><strong>Approvity</strong></a>
                — tailored funding to support businesses of all sizes.
              </p>
            </div>

            <div class="service-block">
              <p class="subtitle">Property Finance</p>
              <p>With access to over 700 lenders across the UK, we cover the full range of property finance — from residential mortgages and buy-to-let, to development finance and bridging loans. Whatever your property ambitions, we help you find the right solution.</p>
            </div>

            <div class="service-block">
              <p class="subtitle">FX Services — Crezco</p>
              <p>Crezco is a platform that allows you to buy and sell foreign currency.</p>

              <p class="subtitle">Why choose Crezco?</p>
              <ul>
                <li>Transparent pricing with no hidden fees — typically more competitive than traditional banks</li>
                <li>Secure and straightforward international payment method</li>
              </ul>

              <p class="caveat">
                <i class="fas fa-info-circle"></i>
                Information on rates and fees is indicative and correct as of 2024. Pricing and terms may have changed — please
                <router-link to="/contact">contact us</router-link>
                for the most up-to-date information.
              </p>

              <p class="subtitle">How to use Crezco?</p>
              <div class="steps">
                <p>Sign up and verify your account</p>
                <p>Add your bank account</p>
                <p>Foreign transaction payment settlement</p>
              </div>

              <div class="button-container">
                <button @click="handleForeignExchange" class="left-btn">Click here to fill in the form</button>
                <button @click="handleForeignExchangeVideo" class="right-btn">Click here to see the training video</button>
              </div>
            </div>
          </div>

          <!-- 3) System -->
          <div v-show="currentCategory === 2" class="tab-pane">
            <h3>System</h3>

            <div class="service-block">
              <p class="subtitle">Cloud Accounting</p>
              <p>Modern cloud-based accounting solutions for efficient business management.</p>
              <p>We also support and are partners with other cloud accounting system providers. Please call to enquire.</p>

              <div class="pricing-info">
                <p class="discount-text">
                  Price information is available on
                  <a href="https://www.sage.com/en-gb/" target="_blank" class="vendor-link">Sage</a>
                  and
                  <a href="https://quickbooks.intuit.com/uk/pricing/" target="_blank" class="vendor-link">QuickBooks</a>
                  websites, as well as other cloud accounting providers. Please contact us for pricing on other platforms.
                </p>
                <div class="discount-badge">
                  <span class="discount-amount">10%</span>
                  <span class="discount-label">Ongoing Discount</span>
                </div>
              </div>
            </div>

            <div class="service-block">
              <p class="subtitle">Payment Solutions</p>
              <p>Integrated payment solutions including
                <a href="https://www.paypal.com/" target="_blank" class="company-link"><strong>PayPal</strong></a>
                and
                <a href="https://sumup.co.uk/" target="_blank" class="company-link"><strong>SumUp</strong></a>.
              </p>
            </div>
          </div>

          <!-- 4) Consultancy / Tax -->
          <div v-show="currentCategory === 3" class="tab-pane">
            <h3>Consultancy / Tax</h3>

            <div class="service-block">
              <p class="subtitle">R&amp;D (Research and Development) Tax Credit</p>
              <p>Maximise your R&amp;D tax relief benefits with our expert guidance — helping you identify qualifying activities and claim the relief your business is entitled to.</p>
            </div>

            <div class="service-block">
              <p class="subtitle">Tax Review</p>
              <p>A comprehensive review of your tax position to identify opportunities, ensure compliance, and optimise your tax efficiency.</p>
            </div>

            <div class="service-block">
              <p class="subtitle">Property Search</p>
              <p>Professional support to help you find the right property — whether for business premises, investment, or development purposes.</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'Services',
  data() {
    return {
      currentCategory: 0,
      categories: [
        { title: 'Set up' },
        { title: 'Finance' },
        { title: 'System' },
        { title: 'Consultancy / Tax' },
      ]
    }
  },
  mounted() {
    // Load Approvity script
    const script = document.createElement('script')
    script.src = 'https://iframe.approvity.com/apply-js'
    document.body.appendChild(script)
  },
  methods: {
    handleForeignExchange() {
      window.open('https://airtable.com/appRiJJslMAUaNf4a/shrYA50W0ejCzY1qA', '_blank');
    },
    handleForeignExchangeVideo() {
      this.$router.push('/training');
    }
  }
} 
</script>

<style scoped>
.services-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  color: #2F4F4F;
}

.main-title {
  color: #1B365D;
  text-align: center;
  margin-bottom: 3rem;
  font-size: 2.5rem;
  font-weight: 600;
  position: relative;
}

.main-title::after {
  content: '';
  display: block;
  width: 60px;
  height: 3px;
  background: #1B365D;
  margin: 1rem auto;
}

.services-section {
  margin-bottom: 4rem;
}

.services-section h2 {
  color: #1B365D;
  margin-bottom: 2rem;
  font-size: 1.8rem;
}

.core-services {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.service-card {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease;
}

.service-card:hover {
  transform: translateY(-5px);
}

.service-card i {
  font-size: 2.5rem;
  color: #1B365D;
  margin-bottom: 1rem;
}

.service-card h3 {
  font-size: 1.35rem;
  margin-bottom: 1rem;
}

h3 {
  color: #2d3436;
  font-size: 2rem;
  margin-bottom: 1.5rem;
  font-weight: 600;
}

p {
  color: #636e72;
  line-height: 1.6;
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

ul {
  margin: 1.5rem 0;
  padding-left: 1.2rem;
}

li {
  color: #636e72;
  margin-bottom: 0.8rem;
  line-height: 1.6;
  position: relative;
  padding-left: 1rem;
}

li::before {
  content: "•";
  color: #007bff;
  font-weight: bold;
  position: absolute;
  left: -1rem;
}

.tabs {
  background: #FFFFFF;
  border-radius: 4px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.tab-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 1rem;
  background: #F8F9FA;
  border-radius: 4px 4px 0 0;
}

.tab-button {
  padding: 0.8rem 1.5rem;
  border: none;
  background: none;
  color: #2F4F4F;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.tab-button.active {
  background: #1B365D;
  color: white;
}

.tab-content {
  padding: 2rem;
}

.tab-pane {
  animation: fadeIn 0.3s ease;
}

.tab-pane h3 {
  color: #1B365D;
  margin-bottom: 1rem;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@media (max-width: 768px) {
  .services-container {
    padding: 1rem;
  }
  
  .tab-buttons {
    flex-direction: column;
  }
  
  .tab-button {
    width: 100%;
    text-align: left;
  }
}

.company-link {
  text-decoration: none;
  color: #1B365D;
  transition: color 0.3s ease;
}

.company-link:hover {
  color: #3498db;
  text-decoration: underline;
}

.company-link strong {
  font-weight: 700;
}

.subtitle {
  color: #1B365D;
  font-weight: 600;
  font-size: 1.15rem;
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
}

.service-block {
  padding-bottom: 1.5rem;
  margin-bottom: 1.5rem;
  border-bottom: 1px solid #e9ecef;
}

.service-block:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.caveat {
  background: #fff8e6;
  border-left: 4px solid #f0ad4e;
  padding: 0.9rem 1rem;
  border-radius: 4px;
  font-size: 0.95rem;
  color: #856404;
  margin: 1rem 0;
}

.caveat i {
  margin-right: 0.4rem;
}

.caveat a {
  color: #1B365D;
  font-weight: 600;
  text-decoration: underline;
}

.steps {
  margin-top: 2rem;
  counter-reset: step;
}

.steps p {
  position: relative;
  padding-left: 2.5rem;
  margin-bottom: 1rem;
}

.steps p::before {
  counter-increment: step;
  content: counter(step);
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 1.8rem;
  height: 1.8rem;
  background: #090909;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.9rem;
  font-weight: bold;
}

button {
  background: #2d3436;
  color: white;
  border: none;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 1rem 0;
  float: right;
  clear: both;
}

button:hover {
  background: #0056b3;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 123, 255, 0.3);
}

button:active {
  transform: translateY(0);
}

/* 添加一个清除浮动的辅助类 */
.clearfix::after {
  content: "";
  display: table;
  clear: both;
}

.pricing-info {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 1.5rem;
  margin-top: 1rem;
  border: 2px solid #e9ecef;
  position: relative;
}

.discount-text {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.vendor-link {
  color: #2d3436;
  text-decoration: none;
  font-weight: 600;
  border-bottom: 2px solid #2d3436;
  transition: all 0.3s ease;
}

.vendor-link:hover {
  opacity: 0.7;
}

.discount-badge {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  background: #2d3436;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  margin-top: 1rem;
}

.discount-amount {
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 1;
}

.discount-label {
  font-size: 0.9rem;
  margin-top: 0.2rem;
}

.left-btn {
  float: left;
  clear: both;
}

.right-btn {
  float: right;
  clear: both;
}

.button-container {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
  width: 100%;
}

.left-btn, .right-btn {
  margin: 0;
}
</style>
