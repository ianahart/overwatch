export let mockCheckLists = [
  {
    id: 1,
    title: 'Checklist A',
    userId: 1,
    isCompleted: false,
    createdAt: '',
    todoCardId: 1,
    checkListItems: [
      {
        id: 101,
        title: 'Item 1',
        isCompleted: false,
        userId: 1,
        checkListId: 1,
      },
    ],
  },
  {
    id: 2,
    title: 'Checklist B',
    userId: 1,
    todoCardId: 1,
    createdAt: '',
    checkListItems: [],
    isCompleted: true,
  },
];
