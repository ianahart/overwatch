const Terms = () => {
  return (
    <div className="text-gray-400 max-w-[750px] w-full p-2">
      <header className="my-2">
        <h1 className="text-2xl font-display font-bold tracking-wide">Terms of Service</h1>
        <p>
          <strong>Effective Date:</strong> 11/20/24
        </p>
      </header>

      <main>
        <section className="my-4">
          <h2>1. Acceptance of Terms</h2>
          <p>
            By accessing or using Overwatch ("the Service"), you agree to comply with and be bound by these Terms of
            Service ("Terms"). If you do not agree to these Terms, you may not access or use the Service.
          </p>
        </section>

        <section className="my-4">
          <h2>2. Description of the Service</h2>
          <p>
            Overwatch is an online platform that connects users seeking code reviews with professional reviewers. The
            Service allows users to create profiles, upload content, engage with reviewers, and process payments
            securely via Stripe.
          </p>
        </section>

        <section className="my-4">
          <h2>3. User Accounts</h2>
          <h3>a. Account Registration</h3>
          <p>
            To use the Service, you must create an account and provide accurate and complete information. You are
            responsible for maintaining the confidentiality of your login credentials and for all activities that occur
            under your account.
          </p>

          <h3>b. Account Termination</h3>
          <p>
            You may delete your account at any time through your account settings. We reserve the right to suspend or
            terminate accounts that violate these Terms or are involved in fraudulent or unlawful activity.
          </p>
        </section>

        <section className="my-4">
          <h2>4. Payments</h2>
          <p>
            All payments are processed securely via Stripe. By making a payment on the Service, you agree to Stripe’s
            <a href="https://stripe.com/legal" target="_blank">
              Terms and Conditions
            </a>
            .
          </p>
          <ul>
            <li>
              <strong>Fees:</strong> The Service may charge fees for code reviews, which will be clearly displayed at
              the time of the transaction.
            </li>
            <li>
              <strong>Refunds:</strong> Refunds are subject to our <a href="#refund-policy">Refund Policy</a>.
            </li>
          </ul>
        </section>

        <section className="my-4">
          <h2>5. User Conduct</h2>
          <p>By using the Service, you agree not to:</p>
          <ul>
            <li>
              Post, upload, or transmit content that is unlawful, offensive, or infringes on the rights of others.
            </li>
            <li>Engage in fraudulent or deceptive practices.</li>
            <li>Attempt to hack, disrupt, or otherwise interfere with the Service.</li>
            <li>Use the Service for any illegal activities.</li>
          </ul>
          <p>Violations may result in account suspension or termination at our sole discretion.</p>
        </section>

        <section className="my-4">
          <h2>6. Content Ownership</h2>
          <p>
            You retain ownership of any content you upload to the Service, including testimonials, comments, and profile
            information. By uploading content, you grant Overwatch a non-exclusive, royalty-free, worldwide license to
            use, display, and distribute your content as part of the Service.
          </p>
        </section>

        <section className="my-4">
          <h2>7. Limitation of Liability</h2>
          <p>
            To the fullest extent permitted by law, Overwatch and its affiliates will not be liable for any indirect,
            incidental, special, or consequential damages arising out of or in connection with your use of the Service.
          </p>
        </section>

        <section className="my-4">
          <h2>8. Modifications to the Terms</h2>
          <p>
            We reserve the right to update or modify these Terms at any time. Changes will be effective immediately upon
            posting on our website. Your continued use of the Service constitutes acceptance of the modified Terms.
          </p>
        </section>

        <section className="my-4">
          <h2>9. Governing Law</h2>
          <p>
            These Terms shall be governed by and construed in accordance with the laws of New Hampshire, USA. Any
            disputes arising from these Terms shall be resolved in the courts of New Hampshire, USA.
          </p>
        </section>

        <section className="my-4">
          <h2>10. Contact Information</h2>
          <p>If you have any questions about these Terms, please contact us at:</p>
          <ul>
            <li>
              <strong>Email:</strong> codeoverwatch@codeoverwatch.com
            </li>
          </ul>
        </section>

        <footer>
          <p>Thank you for using Overwatch!</p>
        </footer>
      </main>
    </div>
  );
};
export default Terms;
