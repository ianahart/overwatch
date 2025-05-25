import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsReportCommentModalContent from '../../../src/components/TopicDetails/TopicDetailsReportCommentModalContent';
import { getLoggedInUser } from '../../utils';
import { toPlainObject } from 'lodash';
import { IUser } from '../../../src/interfaces';
import userEvent from '@testing-library/user-event';
import { server } from '../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../src/util';

describe('TopicDetailsReportCommentModalContent', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (user: IUser, overrides = {}) => {
    return {
      currentUserId: user.id,
      commentId: 1,
      content: 'report comment content',
      closeModal: vi.fn(),
      ...overrides,
    };
  };

  const getForm = () => {
    return {
      getSelect: () => screen.getByLabelText(/reason for reporting/i),
      getTextarea: () => screen.getByLabelText(/additional details/i),
      getReportButton: () => screen.getByRole('button', { name: /report/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(toPlainObject(curUser));

    render(<TopicDetailsReportCommentModalContent {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      form: () => getForm(),
    };
  };

  it('should render elements correctly', () => {
    const { form } = renderComponent();

    expect(screen.getByRole('heading', { name: /report this comment/i })).toBeInTheDocument();
    expect(screen.getByText(/comment under question:/i)).toBeInTheDocument();
    expect(form().getTextarea()).toBeInTheDocument();
    expect(form().getReportButton()).toBeInTheDocument();
    expect(form().getCancelButton()).toBeInTheDocument();
  });

  it('should show error if form is submitted without selecting reason or adding details', async () => {
    const { user, form } = renderComponent();

    await user.click(form().getReportButton());

    expect(await screen.findByText(/details must be between 1 and 400/i)).toBeInTheDocument();
  });

  it('should submit valid form and close modal', async () => {
    const { user, form, props } = renderComponent();

    await user.selectOptions(form().getSelect(), 'SPAM');
    await user.type(form().getTextarea(), 'some additional details');
    await user.click(form().getReportButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });

  it('should call "closeModal" when "Cancel" is clicked', async () => {
    const { user, form, props } = renderComponent();

    await user.click(form().getCancelButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });

  it('should handle API error and display error message', async () => {
    server.use(
      http.post(`${baseURL}/report-comments`, () => {
        return HttpResponse.json(
          {
            message: 'missing details',
          },
          { status: 400 }
        );
      })
    );

    const { user, form } = renderComponent();

    await user.selectOptions(form().getSelect(), 'SPAM');
    await user.type(form().getTextarea(), 'test');

    await user.click(form().getReportButton());

    expect(await screen.findByText('missing details')).toBeInTheDocument();
  });
});
