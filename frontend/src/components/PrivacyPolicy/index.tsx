const PrivacyPolicy = () => {
  return (
    <div className="text-gray-400 max-w-[750px] w-full p-2">
      <header>
        <h1 className="text-3xl font-display my-2">Privacy Policy</h1>
        <p>
          <strong>Effective Date:</strong> 11/20/2024
        </p>
      </header>

      <main>
        <section className="my-2">
          <h2>1. Information We Collect</h2>
          <p>We collect various types of information to provide and improve our services. This includes:</p>

          <h3>a. Personal Information Provided by You</h3>
          <ul>
            <li>
              <strong>Account Information:</strong> When you create an account, we collect your name, email address, and
              password.
            </li>
            <li>
              <strong>Profile Information:</strong> If you choose to upload a profile picture or additional personal
              details, this information will be stored in your profile.
            </li>
            <li>
              <strong>Payment Information:</strong> Payment details are processed securely via Stripe; we do not store
              your payment card details.
            </li>
            <li>
              <strong>Testimonials and Comments:</strong> Content you choose to share, such as testimonials and
              comments, will be collected and displayed on the platform.
            </li>
          </ul>

          <h3>b. Automatically Collected Information</h3>
          <ul>
            <li>
              <strong>Usage Data:</strong> We collect data about how you interact with our platform, such as pages
              viewed, time spent on the site, and clicks.
            </li>
            <li>
              <strong>Device Information:</strong> This includes your IP address, browser type, operating system, and
              other technical details.
            </li>
          </ul>

          <h3>c. Third-Party Services</h3>
          <ul>
            <li>
              <strong>Stripe Payments:</strong> Stripe processes all payment-related data. For more information, refer
              to{' '}
              <a href="https://stripe.com/privacy" target="_blank">
                Stripe's Privacy Policy
              </a>
              .
            </li>
          </ul>
        </section>

        <section className="my-2">
          <h2>2. How We Use Your Information</h2>
          <p>We use your information for the following purposes:</p>
          <ul>
            <li>To create and manage your account.</li>
            <li>To facilitate transactions and process payments via Stripe.</li>
            <li>To provide, personalize, and improve our services.</li>
            <li>To communicate with you regarding updates, support, and promotional content.</li>
            <li>To analyze usage patterns and improve our platform.</li>
          </ul>
        </section>

        <section className="my-2">
          <h2>3. Your Rights</h2>
          <h3>a. Access and Update</h3>
          <p>You can view and update your personal information through your account settings.</p>

          <h3>b. Account Deletion</h3>
          <p>
            You have the right to delete your account at any time. Deleting your account will remove your profile
            information, comments, and testimonials from our platform.
          </p>

          <h3>c. Communication Preferences</h3>
          <p>
            You can opt-out of promotional emails by following the unsubscribe link in any email. However, you may still
            receive essential account-related communications.
          </p>
        </section>

        <section className="my-2">
          <h2>4. How We Protect Your Information</h2>
          <p>
            We implement industry-standard security measures to safeguard your data. This includes encrypted connections
            (HTTPS) and secure data storage. However, no method of transmission over the internet is completely secure,
            and we cannot guarantee absolute security.
          </p>
        </section>

        <section className="my-2">
          <h2>5. Third-Party Sharing</h2>
          <p>We do not sell your personal information to third parties. However, we may share your information:</p>
          <ul>
            <li>
              <strong>With Service Providers:</strong> For example, with Stripe to facilitate payments.
            </li>
            <li>
              <strong>For Legal Reasons:</strong> To comply with legal obligations or respond to lawful requests.
            </li>
          </ul>
        </section>

        <section className="my-2">
          <h2>6. Cookies and Tracking Technologies</h2>
          <p>We use cookies and similar technologies to enhance your experience. These may be used for:</p>
          <ul>
            <li>Remembering your preferences.</li>
            <li>Analyzing site usage to improve performance.</li>
            <li>Offering personalized content.</li>
          </ul>
          <p>You can manage cookie preferences through your browser settings.</p>
        </section>

        <section className="my-2">
          <h2>7. Children’s Privacy</h2>
          <p>
            Our platform is not intended for individuals under the age of 13, and we do not knowingly collect personal
            information from children.
          </p>
        </section>

        <section className="my-2">
          <h2>8. Changes to This Privacy Policy</h2>
          <p>
            We may update this Privacy Policy from time to time. Changes will be effective upon posting on our website,
            and we will notify you of significant updates.
          </p>
        </section>

        <section className="my-2">
          <h2>9. Contact Us</h2>
          <p>If you have any questions about this Privacy Policy or how your data is handled, please contact us at:</p>
          <ul>
            <li>
              <strong>Email:</strong> codeoverwatch@codeoverwatch.com
            </li>
          </ul>
        </section>
      </main>

      <footer>
        <p>By using Overwatch, you agree to this Privacy Policy. Thank you for trusting us with your information!</p>
      </footer>
    </div>
  );
};

export default PrivacyPolicy;
